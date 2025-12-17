"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Swal from 'sweetalert2';
import Link from "next/link";
import { ServerErrorMessage } from "next/dist/server/dev/hot-reloader-types";

export interface EstadiaDTO {
    idEstadia: number;
    precio: number;
    horaCheckin: string;   // "14:00"
    horaCheckout: string;  // "10:00"
    idReserva: number;
    idHabitacion: number;
}
export interface Ocupante {
    idDetalleEstadia: number,
    idEstadia: number,
    dniHuesped: number,
    nombre: string,
    apellido: string
}

export default function OcuparHabitacionPage() {
    const router = useRouter();

    const [paso, setPaso] = useState<1 | 2 | 3 | 4>(1);
    const [loading, setLoading] = useState(false);

    const [idHabitacion, setIdHabitacion] = useState<number>();
    const [horaCheckout, setHoraCheckout] = useState<string>("");

    const [estadia, setEstadia] = useState<EstadiaDTO>();
    const [ocupantes, setOcupantes] = useState<Ocupante[]>([]);
    
    const [ocupanteSeleccionado, setOcupanteSeleccionado] = useState<number | null>(null);

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
        
    }

    const buscarOcupantes = async (estadiaData : EstadiaDTO) => {
        try {
            setLoading(true);
            
            const url = `http://localhost:8081/facturacion/estadia/${estadiaData.idEstadia}/ocupantes`;
            const res = await fetch(url);
            if (!res.ok) throw new Error("Error al conectar con backend");
            const data = await res.json();
            if (!data) {
                Toast.fire({
                    icon: "warning",
                    title: "No existen ocupantes asociados a la estadia."
                });
            }
            else {
                setOcupantes(data);
                setPaso(2)
            }
        } catch (e) {
            console.error(e);
            Swal.fire("Error", "No existen ocupantes asociados a la estadia", "error");
        } finally {
            setLoading(false);
        }
    }

    const buscarEstadia = async () => {
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

                const url = `http://localhost:8081/facturacion/estadia?numeroHabitacion=${idHabitacion}&horaCheckout=${horaCheckout}`;
                const res = await fetch(url);
                if (!res.ok) throw new Error("Error al conectar con backend");
                const data = await res.json();
                if (!data) {
                    Toast.fire({
                        icon: "warning",
                        title: "No se pudo encontrar una estadia que corresponda a esos datos."
                    });
                    setIdHabitacion(undefined)
                    setHoraCheckout("")
                }
                else {
                    setEstadia(data);
                    buscarOcupantes(data)
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
                                    onClick={buscarEstadia} 
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
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
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
                    {( paso === 3 && (
                        <div>
                            <button
                                //onClick={} 
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
                    ))}
                    <div className="w-full flex justify-i mt-0">
                        <button
                            type="button"
                            onClick={() => router.back()}
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