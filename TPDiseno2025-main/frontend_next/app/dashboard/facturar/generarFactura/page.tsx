"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Swal from 'sweetalert2';
import Link from "next/link";
import { ServerErrorMessage } from "next/dist/server/dev/hot-reloader-types";

interface EstadiaDTO {
    idEstadia: number,
    precio: number,
    horaCheckin: string,
    horaCheckout: string,
    idHabitacion: number,
}
interface ResponsableDTO {
    juridica: JuridicaDTO,
    razonSocial: string
}
interface JuridicaDTO {
    cuit: number,
    razonSocial: string,
    direccion: DireccionDTO
}
interface DireccionDTO {
    calle: string,
    numero: number,
    departamento?: string,
    piso?: number,
    codPostal: number,
    localidad: string,
    ciudad: string,
    provincia: string,
    pais: string
}
interface Ocupante {
    idDetalleEstadia: number,
    idEstadia: number,
    dniHuesped: number,
    nombre: string,
    apellido: string,
    edad: number,
    direccion: DireccionDTO
}
interface ResponsablePagoResponseDTO {
    idResponsablePago: number,
    razonSocial: string
}
interface FacturaPreviewDTO {
    precioEstadia: number,
    consumibles: ConsumibleDTO[],
    total: number,
    tipoFactura: string
}
interface FacturaRequestDTO {
    idEstadia: number,
    consumiblesSeleccionados: number[],
    incluirEstadia: boolean,
    responsablePago: ResponsableDTORequest
}
interface ResponsableDTORequest {
    idResponsable: number,
    juridica: JuridicaDTO,
    razonSocial: string
}
interface ConsumibleDTO {
    idConsumible: number,
    nombre: string,
    precio: number
}
interface Factura {
    idFactura: number,
    idResponsablePago: ResponsableDTO,
    precioFinal: number,
    idEstadia: EstadiaDTO,
    nroFactura: number,
    tipoFactura: string,
    fecha: string
}

export default function OcuparHabitacionPage() {
    const router = useRouter();

    const [paso, setPaso] = useState<1 | 2 | 3 | 4>(1);
    const [loading, setLoading] = useState(false);

    const [idHabitacion, setIdHabitacion] = useState<number>();
    const [horaCheckout, setHoraCheckout] = useState<string>("");

    const [estadia, setEstadia] = useState<EstadiaDTO>();
    const [ocupantes, setOcupantes] = useState<Ocupante[]>([]);

    const [responsable, setResponsable] = useState<ResponsableDTO>();
    const [cuil, setCuil] = useState<number | null>(null);
    const [responsablePagoResponse, setResponsablePagoResponse] = useState<ResponsablePagoResponseDTO>();
    const [facturaPreview, setFacturaPreview] = useState<FacturaPreviewDTO>();
    
    const [ocupanteSeleccionado, setOcupanteSeleccionado] = useState<number | null>(null);
    const [ocupante, setOcupante] = useState<Ocupante>();

    const [incluirEstadia, setIncluirEstadia] = useState<boolean>(false);
    const [consumiblesIncluidos, setConsumiblesIncluidos] = useState<number[]>([]);

    const [facturaConfirmada, setFacturaConfirmada] = useState<Factura>();

    const toggleConsumible = (id: number) => {
        setConsumiblesIncluidos(prev =>
            prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
        );
    };

    const totalSeleccionado = (() => {
        if (!facturaPreview) return 0;

        const totalCons = (facturaPreview.consumibles ?? [])
            .filter(c => consumiblesIncluidos.includes(c.idConsumible))
            .reduce((acc, c) => acc + (c.precio ?? 0), 0);

        const totalEstadia = incluirEstadia ? (facturaPreview.precioEstadia ?? 0) : 0;

        return totalEstadia + totalCons;
    })();

    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    });

    const facturar = async () => {
        if (!ocupanteSeleccionado) {
            Toast.fire({
                icon: "warning",
                title: "Seleccione un ocupante para facturar."
            });
        }
        const ocupanteData = ocupantes.find(
            o => o.idDetalleEstadia === ocupanteSeleccionado
        )
        setOcupante(ocupanteData)
        if (ocupanteData && estadia && cuil) {
            const juridicaData : JuridicaDTO = {
                cuit: cuil,
                direccion: ocupanteData.direccion,
                razonSocial: `${ocupanteData.apellido} ${ocupanteData.nombre}`
            }
            await definirResponsable(estadia, ocupanteData, juridicaData);
            setPaso(3)
        }
        else {
            setPaso(1)
            Toast.fire({
                icon: "warning",
                title: "Ha ocurido un error con los datos seleccionados."
            });
        }
    }
    const definirResponsable = async (estadiaData: EstadiaDTO, ocupandeData : Ocupante, juridicaData : JuridicaDTO) => {
        try {
            if (!estadiaData?.idEstadia) throw new Error("Falta idEstadia");
            if (!ocupanteSeleccionado) {
            Toast.fire({ icon: "warning", title: "Seleccione un ocupante para facturar." });
            return;
            }

            const url = `http://localhost:8081/facturacion/responsable?idEstadia=${estadiaData.idEstadia}`;
            const responsablePagoData : ResponsableDTO = {
                juridica: juridicaData,
                razonSocial: `${ocupandeData.apellido} ${ocupandeData.nombre}`
            }

            const res = await fetch(url, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    idDetalleEstadia: ocupanteSeleccionado,
                    request: responsablePagoData
                })
            });

            if (!res.ok) {
                const txt = await res.text();
                throw new Error(txt || `Error HTTP ${res.status}`);
            }

            const data = await res.json();
            setResponsable(responsablePagoData)
            setResponsablePagoResponse(data)

            if (responsablePagoResponse) {
                await obtenerPreview(estadiaData.idEstadia, responsablePagoResponse.idResponsablePago)
            }

            console.log(`Responsable definido: ${data.razonSocial}`)
        } catch (e) {
            Toast.fire({ icon: "error", title: `${e}` });
            setPaso(1)
        }
    };

    const obtenerPreview = async (idEstadia : number, idResponsable : number) => {
        try {
            const url = `http://localhost:8081/facturacion/preview?idEstadia=${idEstadia}&=idResponsablePago${idResponsable}`;
            const res = await fetch(url);

            if (!res.ok) {
                const txt = await res.text();
                throw new Error(txt || `Error HTTP ${res.status}`);
            }

            const data = await res.json();
            setFacturaPreview(data)
        } catch (e) {
            Toast.fire({ icon: "error", title: `${e}` });
            setPaso(1)
        }
    }

    const confirmarFactura = async () => {
        if (responsablePagoResponse && responsable && estadia) {
            try {
                const url = `http://localhost:8081/facturacion/generar`;
                const responsablePagoReq : ResponsableDTORequest = {
                    idResponsable: responsablePagoResponse.idResponsablePago,
                    juridica: responsable.juridica,
                    razonSocial: responsable.razonSocial
                }
                const facturaData : FacturaRequestDTO = {
                    idEstadia: estadia.idEstadia,
                    responsablePago: responsablePagoReq,
                    incluirEstadia: incluirEstadia,
                    consumiblesSeleccionados: consumiblesIncluidos
                }
                const res = await fetch(url, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        request: facturaData
                    })
                });

                if (!res.ok) {
                    const txt = await res.text();
                    throw new Error(txt || `Error HTTP ${res.status}`);
                }

                const data = await res.json();
                setFacturaConfirmada(data)
                setPaso(4)
            } catch (e) {
                Toast.fire({ icon: "error", title: `${e}` });
                setPaso(1)
            }
        }
        else {
            setPaso(1)
            Toast.fire({ icon: "error", title: `Error al confirmar factura` });
        }
    }

    // const buscarOcupantes = async (dataCheckout : any) => {
    //     try {
    //         setLoading(true);
            
    //         const url = `http://localhost:8081/facturacion/estadia/${estadiaData.idEstadia}/ocupantes`;
    //         const res = await fetch(url);
    //         if (!res.ok) throw new Error("Error al conectar con backend");
    //         const data = await res.json();
    //         if (!data) {
    //             Toast.fire({
    //                 icon: "warning",
    //                 title: "No existen ocupantes asociados a la estadia."
    //             });
    //         }
    //         else {
    //             setOcupantes(data);
    //             setPaso(2)
    //         }
    //     } catch (e) {
    //         console.error(e);
    //         Swal.fire("Error", "No existen ocupantes asociados a la estadia", "error");
    //     } finally {
    //         setLoading(false);
    //     }
    // }

    const buscarCheckout = async () => {
        let errorCampos = false
        const errors : string[] = []
        let errorMsg = ""
        if (idHabitacion == null || idHabitacion == undefined) {
            errors.push("El campo numero habitacion no puede estar vacio")
            errorCampos = true
        }
        else if (idHabitacion < 1) {
            errors.push("El campo numero habitacion no puede ser un numero menor a 1")
            errorCampos = true
        }
        if (horaCheckout === "") {
            errors.push("El campo hora de salida no puede estar vacio")
            errorCampos = true
        }
        if (errorCampos) {
            errors.forEach((msg, idx) => {errorMsg += (msg + ((idx == errorMsg.length-1) ? ".": (errorMsg.length > 1? "" : ", ")))})
            Toast.fire({
                icon: "warning",
                title: `Error: ${errorMsg}`
            });
        }
        else {
            try {
                setLoading(true);

                const url = `http://localhost:8081/facturacion/checkout?numeroHabitacion=${idHabitacion}&horaCheckout=${horaCheckout}`;
                const res = await fetch(url);
                if (!res.ok) throw new Error("Error al conectar con backend");
                const data = await res.json();
                if (!data) {
                    Toast.fire({
                        icon: "warning",
                        title: "No se encontro checkout."
                    });
                    setIdHabitacion(undefined)
                    setHoraCheckout("")
                }
                else {
                    try {
                        setLoading(true);

                        const url = `http://localhost:8081/estadias/estadia?&numeroHabitacion=${idHabitacion}&horaCheckout=${horaCheckout}`;
                        const res = await fetch(url);
                        if (!res.ok) throw new Error("Error al conectar con backend");
                        const dataEstadia = await res.json();
                        if (!dataEstadia) {
                            Toast.fire({
                                icon: "warning",
                                title: "No se pudo encontrar una estadia que corresponda a esos datos."
                            });
                            setIdHabitacion(undefined)
                            setHoraCheckout("")
                        }
                        else {
                            setEstadia(dataEstadia);
                        }
                    } catch (e) {
                        console.error(e);
                        Swal.fire("Error", "No existe una estadia con los datos correspondientes", "error");
                    } finally {
                        setLoading(false);
                    }
                    setOcupantes(data);
                    setPaso(2);
                    // buscarOcupantes(data)
                }
            } catch (e) {
                console.error(e);
                Swal.fire("Error", "No existe una estadia con los datos correspondientes, revise los campos.", "error");
            } finally {
                setLoading(false);
            }
        }
    }


    return (
        <div className="p-3 max-w-6xl mx-auto font-sans text-gray-800">
            <div className="mb-8 flex justify-center items-center bg-gray-100 p-0.5 rounded-lg">
                <div className={`font-bold text-4xl 'text-blue-600' : 'text-gray-400'}`}>Generar Factura</div>
            </div>
            <div className="bg-white shadow rounded-lg p-6 border">
                <div className="flex gap-4 items-end flex-wrap">
                    {paso === 1 && (
                        <div>
                            <div className="mb-2 text-sm text-gray-400">
                                Introduzca el número de habitación y hora de salida (Ej: 10:00) correspondientes a una estadia.
                            </div>
                            <div className="flex gap-4 mb-6 items-end flex-wrap">
                                <div>
                                    <label className="block text-sm font-bold text-gray-700">*Numero de habitación</label>
                                    <input type="text" 
                                            className="border p-2 rounded"
                                            value={idHabitacion}
                                            onChange={e => setIdHabitacion(Number(e.target.value))} 
                                    />
                                </div>
                                <div className="relative">
                                    <label className="block text-sm font-bold text-gray-700">*Hora de salida</label>
                                    <input type="text" 
                                            className="border p-2 rounded"
                                            value={horaCheckout} 
                                            placeholder="10:00"
                                            onChange={e => setHoraCheckout(e.target.value)} 
                                    />
                                </div>

                                <button
                                    onClick={buscarCheckout} 
                                    className={`px-6 py-2 rounded font-bold text-white ${
                                        loading
                                        ? 'bg-gray-400 cursor-not-allowed' 
                                        : 'bg-blue-600 hover:bg-blue-700'
                                    }`}
                                    disabled={loading}
                                >
                                    {loading ? "Cargando..." : "Buscar"}
                                </button>
                            </div>
                        </div>
                    )}
                    {( paso === 2 && (
                        <div>
                            <div className="overflow-x-auto mb-4">
                                <div className="mb-2">Seleccione un ocupante para facturar:</div>
                                <table className="min-w-full border text-sm">
                                    <thead>
                                    <tr className="border-b">
                                        <th className="p-2 w-10"></th>
                                        <th className="p-2 text-left">DNI</th>
                                        <th className="p-2 text-left">Nombre</th>
                                        <th className="p-2 text-left">Apellido</th>
                                        <th className="p-2 text-left">Edad</th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    {ocupantes.map(o => (
                                        <tr key={o.idDetalleEstadia} className="border-b" onClick={() => setOcupanteSeleccionado(o.idDetalleEstadia)}>
                                            <td className="p-2">
                                                <input
                                                type="radio"
                                                name="ocupante"
                                                checked={ocupanteSeleccionado === o.idDetalleEstadia}
                                                onChange={() => setOcupanteSeleccionado(o.idDetalleEstadia)}
                                                />
                                            </td>

                                            <td className="p-2">{o.dniHuesped}</td>
                                            <td className="p-2">{o.nombre}</td>
                                            <td className="p-2">{o.apellido}</td>
                                            <td className="p-2">{o.edad}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                            {( ocupanteSeleccionado !== null && (
                                <div>
                                    <label className="block text-sm font-bold text-gray-700">Ingrese el CUIT/CUIL del ocupante a facturar:</label>
                                    <input type="text"
                                            className="border p-2 rounded"
                                            value={horaCheckout}
                                            onChange={e => setCuil(Number(e.target.value))} 
                                    />
                                </div>
                            ))}
                            <div>
                                <button
                                    onClick={facturar} 
                                    className={`px-6 py-2 rounded font-bold text-white mb-8 ${
                                        loading 
                                        ? 'bg-gray-400 cursor-not-allowed' 
                                        : 'bg-blue-600 hover:bg-blue-700'
                                    }`}
                                    disabled={loading}
                                >
                                    {loading ? "Cargando..." : "Facturar"}
                                </button>
                            </div>
                        </div>
                    ))}
                    {( paso === 3 && ocupante && estadia && facturaPreview && (
                        <div>
                            <div className="mb-2">{ocupante.nombre}</div>
                                <ul className="mb-4 space-y-2">
                                    <li className="flex items-center gap-2">
                                        <input
                                        type="checkbox"
                                        checked={incluirEstadia}
                                        onChange={(e) => setIncluirEstadia(e.target.checked)}
                                        />
                                        <span>
                                        Valor estadia: <strong>${facturaPreview.precioEstadia}</strong>
                                        </span>
                                    </li>
                                    <li>
                                        <div className="font-semibold mb-1">Consumibles:</div>
                                        <ul className="pl-4 space-y-1">
                                        {facturaPreview.consumibles.map(c => (
                                            <li key={c.idConsumible} className="flex items-center gap-2">
                                            <input
                                                type="checkbox"
                                                checked={consumiblesIncluidos.includes(c.idConsumible)}
                                                onChange={() => toggleConsumible(c.idConsumible)}
                                            />
                                            <span>
                                                {c.nombre}: <strong>${c.precio}</strong>
                                            </span>
                                            </li>
                                        ))}
                                        </ul>
                                    </li>
                                    <li>
                                        Total seleccionado: <strong>${totalSeleccionado}</strong>
                                    </li>
                                    <li>
                                        Tipo de factura: <strong>{facturaPreview.tipoFactura}</strong>
                                    </li>
                                </ul>
                            <div>
                                <button
                                    onClick={confirmarFactura} 
                                    className={`px-6 py-2 rounded font-bold text-white ${
                                        loading 
                                        ? 'bg-gray-400 cursor-not-allowed' 
                                        : 'bg-blue-600 hover:bg-blue-700'
                                    }`}
                                    disabled={loading}
                                >
                                    {loading ? "Cargando..." : "Aceptar"}
                                </button>
                            </div>
                        </div>
                    ))}
                    {( paso === 4 && facturaConfirmada && (
                        <div>
                            <ul className="pl-6 space-y-1 mb-2">
                                <li>
                                    <strong>ID Factura:</strong> {facturaConfirmada.idFactura}
                                </li>
                                <li>
                                    <strong>Nro Factura:</strong> {facturaConfirmada.nroFactura}
                                </li>
                                <li>
                                    <strong>Tipo Factura:</strong> {facturaConfirmada.tipoFactura}
                                </li>
                                <li>
                                    <strong>Fecha:</strong> {facturaConfirmada.fecha}
                                </li>
                                <li>
                                    <strong>Precio Final:</strong> ${facturaConfirmada.precioFinal}
                                </li>
                                <li>
                                    <strong>Responsable de Pago:</strong>
                                    <ul className="list-disc pl-6">
                                    {facturaConfirmada.idResponsablePago.juridica && (
                                        <li>
                                            <strong>Razón Social:</strong> {facturaConfirmada.idResponsablePago.juridica.razonSocial}
                                            <strong>CUIT:</strong> {facturaConfirmada.idResponsablePago.juridica.cuit}
                                        </li>
                                    )}
                                    </ul>
                                </li>
                                <li>
                                    <strong>Estadía:</strong>
                                    <ul className="list-disc pl-6">
                                        <li>
                                            <strong>ID Estadía:</strong> {facturaConfirmada.idEstadia.idEstadia}
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                            <div>
                                <button
                                    onClick={() => router.replace("/dashboard/facturar")} 
                                    className={`px-6 py-2 rounded font-bold text-white ${
                                        loading 
                                        ? 'bg-gray-400 cursor-not-allowed' 
                                        : 'bg-blue-600 hover:bg-blue-700'
                                    }`}
                                    disabled={loading}
                                >
                                    {loading ? "Cargando..." : "Finalizar"}
                                </button>
                            </div>
                        </div>
                    ))}
                    <div className="w-full flex justify-i mt-0">
                        <button
                            type="button"
                            onClick={() => router.replace("/dashboard/facturar")}
                            className="px-6 py-2 bg-gray-300 text-gray-800 font-bold rounded hover:bg-gray-400 transition-colors"
                        >
                            Volver al menu
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}