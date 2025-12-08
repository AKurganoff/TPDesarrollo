"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Swal from 'sweetalert2'; 

// TIPOS 
interface CeldaCalendario {
  fecha: string;
  idHabitacion: number;
  estado: "LIBRE" | "OCUPADA" | "RESERVADA";
}

interface HuespedForm {
  dni: string;
  nombre: string;
  apellido: string;
  telefono: string;
}

interface TipoHabitacion {
    idTipo: number;
    nombreTipo: string;
    precioNoche: number;
}

export default function CrearReservaPage() {
    const router = useRouter();
    
    // ESTADOS 
    const [paso, setPaso] = useState<1 | 2 | 3>(1);
    const [loading, setLoading] = useState(false);

    //  Búsqueda y Selección
    const [fechaDesde, setFechaDesde] = useState("");
    const [fechaHasta, setFechaHasta] = useState("");
    const [disponibilidad, setDisponibilidad] = useState<CeldaCalendario[]>([]);
    const [tiposHabitacion, setTiposHabitacion] = useState<TipoHabitacion[]>([]);
    const [tipoSeleccionado, setTipoSeleccionado] = useState<string>(""); 
    
    // Selección temporal
    const [seleccion, setSeleccion] = useState<{
        idHabitacion: number;
        fechaInicio: string;
        fechaFin: string;
        cantNoches: number;
    } | null>(null);

    //  Datos Huésped
    const [huesped, setHuesped] = useState<HuespedForm>({
        dni: "",
        nombre: "",
        apellido: "",
        telefono: "",
    });

    // Validación visual de fechas
    const errorFechas = fechaDesde && fechaHasta && fechaHasta < fechaDesde;

    // --- CONFIGURACIÓN SWEETALERT ---
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

    useEffect(() => {
        // Cargar tipos de habitación
        const fetchTipos = async () => {
            try {
                const res = await fetch("http://localhost:8081/tiposhabitacion");
                if (res.ok) {
                    const data = await res.json();
                    setTiposHabitacion(data);
                }
            } catch (error) {
                console.error("Error cargando tipos:", error);
            }
        };
        fetchTipos();

        // Verificar reserva pendiente (si vuelve de crear huesped)
        const reservaPendiente = localStorage.getItem("reservaPendiente");
        
        if (reservaPendiente) {
            const data = JSON.parse(reservaPendiente);
            setSeleccion(data.seleccion);
            setHuesped(prev => ({ ...prev, dni: data.dni }));
            setFechaDesde(data.seleccion.fechaInicio);
            setFechaHasta(data.seleccion.fechaFin);
            setPaso(3);
        }
    }, []);

    // --- CANCELAR  ---
    const handleCancelarProceso = () => {
        // Redirige al listado principal o al dashboard
        router.push("/dashboard/reservas");
    };

    // ---  MOSTRAR ESTADO HABITACIONES ---
    const buscarDisponibilidad = async () => {
        if(!fechaDesde || !fechaHasta) {
            Toast.fire({
                icon: "warning",
                title: "Por favor seleccione un rango de fechas."
            });
            return;
        }
        setLoading(true);
        try {
            let url = `http://localhost:8081/reservas/disponibilidad?fechaInicio=${fechaDesde}&fechaFin=${fechaHasta}`;
            
            if (tipoSeleccionado) {
                url += `&idTipo=${tipoSeleccionado}`;
            }
            const res = await fetch(url);
            if (!res.ok) throw new Error("Error al conectar con backend");
            const data = await res.json();
            setDisponibilidad(data);
        } catch (e) {
            console.error(e);
            Swal.fire("Error", "No se pudo conectar con el servidor.", "error");
        } finally {
            setLoading(false);
        }
    };

    const seleccionarCelda = (celda: CeldaCalendario, rowHasBlockedCells: boolean) => {
        if (celda.estado !== "LIBRE" || rowHasBlockedCells) {
            Toast.fire({
                icon: "error",
                title: "La habitación seleccionada no está disponible."
            });
            return;
        }

        const fInicio = new Date(fechaDesde);
        const fFin = new Date(fechaHasta);
        const diffTime = Math.abs(fFin.getTime() - fInicio.getTime());
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 

        setSeleccion({
            idHabitacion: celda.idHabitacion,
            fechaInicio: fechaDesde,
            fechaFin: fechaHasta,
            cantNoches: diffDays +1
        });
    };

    // ---  VERIFICACIÓN ---
    const handleAceptarVerificacion = () => {
        setPaso(3);
    };

    const handleRechazarVerificacion = () => {
        setSeleccion(null);
        setPaso(1);
    };

    // --- CONFIRMAR CON SWEETALERT ---
    const finalizarReserva = async () => {
        if(!seleccion) return;
        
        if (!huesped.dni) {
            Toast.fire({
                icon: "warning",
                title: "Por favor ingrese el DNI del huésped."
            });
            return;
        }

        setLoading(true);

        try {
            // Verificar si el huésped existe
            const checkRes = await fetch(`http://localhost:8081/huespedes/getByDni?dni=${huesped.dni}`);

            if (checkRes.status === 404) {
                setLoading(false);
                
                // MODAL 
                const resultado = await Swal.fire({
                    title: 'Huésped no encontrado',
                    text: `El DNI ${huesped.dni} no figura en el sistema. ¿Desea registrarlo ahora?`,
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Sí, registrar',
                    cancelButtonText: 'Cancelar'
                });

                if (resultado.isConfirmed) {
                    const estadoAGuardar = {
                        seleccion: seleccion,
                        dni: huesped.dni
                    };
                    localStorage.setItem("reservaPendiente", JSON.stringify(estadoAGuardar));
                    router.push("/dashboard/huesped/altaHuesped?returnTo=reserva"); 
                }
                return; 
            }

            // --- HUÉSPED SÍ EXISTE ---
            const payload = {
                reserva: {
                    cantHuesped: 1,
                    fechaInicio: seleccion.fechaInicio,
                    cantNoches: seleccion.cantNoches,
                    descuento: false,
                    estado: "Confirmada",
                    idHuesped: parseInt(huesped.dni)
                },
                huesped: {
                    dni: parseInt(huesped.dni),
                    nombre: "",   
                    apellido: "", 
                    tipoDni: "DNI",
                    direccion: null 
                },
                habitacion: {
                    idHabitacion: seleccion.idHabitacion,
                    idTipo: 1, 
                    estado: "Reservada", 
                    nochesDescuento: 0
                }
            };

            const res = await fetch("http://localhost:8081/reservas/crear", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });

            if(res.ok) {
                // EXITO: TOAST VERDE
                Toast.fire({
                    icon: "success",
                    title: "¡Reserva registrada con éxito!"
                });
                
                localStorage.removeItem("reservaPendiente");
                
                setTimeout(() => {
                    router.push("/dashboard/reservas");
                }, 1500);

            } else {
                const txt = await res.text();
                Swal.fire({
                    icon: "error",
                    title: "Error al registrar",
                    text: txt
                });
            }

        } catch (e) {
            console.error(e);
            Toast.fire({
                icon: "error",
                title: "Error de conexión con el servidor."
            });
        } finally {
            setLoading(false);
        }
    };

    // --- RENDERIZADO ---
    const getDias = () => Array.from(new Set(disponibilidad.map(c => c.fecha))).sort();
    const getHabitaciones = () => Array.from(new Set(disponibilidad.map(c => c.idHabitacion))).sort((a,b) => a-b);

    return (
        <div className="p-3 max-w-6xl mx-auto font-sans text-gray-800">
        
        {/* HEADER */}
        <div className="mb-8 flex justify-between items-center bg-gray-100 p-0.5 rounded-lg">
            <div className={`font-bold ${paso === 1 ? 'text-blue-600' : 'text-gray-400'}`}>1. Selección</div>
            <div className="text-gray-400">→</div>
            <div className={`font-bold ${paso === 2 ? 'text-blue-600' : 'text-gray-400'}`}>2. Verificación</div>
            <div className="text-gray-400">→</div>
            <div className={`font-bold ${paso === 3 ? 'text-blue-600' : 'text-gray-400'}`}>3. Datos Huésped</div>
        </div>

        {/* --- GRILLA DE DISPONIBILIDAD --- */}
        {paso === 1 && (
            <div className="bg-white shadow rounded-lg p-6 border">
                
                <div className="flex gap-4 mb-6 items-end flex-wrap">
                    <div>
                        <label className="block text-sm font-bold text-gray-700">Desde</label>
                        <input type="date" className="border p-2 rounded" value={fechaDesde} onChange={e => setFechaDesde(e.target.value)} />
                    </div>
                    <div className="relative">
                        <label className="block text-sm font-bold text-gray-700">Hasta</label>
                        <input type="date" 
                                className={`border p-2 rounded ${errorFechas ? 'border-red-500 bg-red-50' : ''}`} 
                                value={fechaHasta} 
                                onChange={e => setFechaHasta(e.target.value)} 
                        />
                        {errorFechas && (
                            <p className="text-red-500 text-xs mt-1 absolute w-48">
                                La fecha de fin debe ser mayor a la fecha de inicio
                            </p>
                        )}
                    </div>
                    
                    <div className="min-w-[200px]">
                        <label className="block text-sm font-bold text-gray-700">Tipo Habitación</label>
                        <select className="border p-2 rounded w-full bg-white"
                                value={tipoSeleccionado}
                                onChange={(e) => setTipoSeleccionado(e.target.value)}>
                            <option value="">Todas las habitaciones</option>
                            {tiposHabitacion.map(tipo => (
                                <option key={tipo.idTipo} value={tipo.idTipo}>
                                    {tipo.nombreTipo}
                                </option>
                            ))}
                        </select>
                    </div>

                    <button
                        onClick={buscarDisponibilidad} 
                        className={`px-6 py-2 rounded font-bold text-white ${
                            loading || errorFechas 
                            ? 'bg-gray-400 cursor-not-allowed' 
                            : 'bg-blue-600 hover:bg-blue-700'
                        }`}
                        disabled={loading || !!errorFechas}
                    >
                        {loading ? "Cargando..." : "Buscar"}
                    </button>
                </div>

                {/* TABLA */}
                {disponibilidad.length > 0 && (
                    <div id="taula" className="relative border rounded overflow-auto max-h-[60vh]">
                        <table className="min-w-full table-fixed text-center border-collapse">
                            <thead className="bg-gray-200 sticky top-0 z-30">
                                <tr>
                                    <th className="p-3 border sticky left-0 top-0 z-40 bg-gray-200 whitespace-nowrap">Habitación</th>
                                    {getDias().map(dia => (
                                        <th key={dia} className="p-3 border w-[100px] whitespace-nowrap">{dia}</th>
                                    ))}
                                </tr>
                            </thead>
                            <tbody>
                                {getHabitaciones().map(idHab => {
                                    // Verificar si la fila tiene celdas ocupadas o reservadas
                                    const rowHasBlockedCells = getDias().some(dia => {
                                        const celda = disponibilidad.find(c => c.idHabitacion === idHab && c.fecha === dia);
                                        const estado = celda ? celda.estado : "DESCONOCIDO";
                                        return estado === "OCUPADA" || estado === "RESERVADA";
                                    });

                                    return (
                                        <tr key={idHab}>
                                            <td className="p-3 border sticky left-0 z-20 font-bold bg-gray-50 w-[200px]">Hab {idHab}</td>
                                            {getDias().map(dia => {
                                                const celda = disponibilidad.find(c => c.idHabitacion === idHab && c.fecha === dia);
                                                const estado = celda ? celda.estado : "DESCONOCIDO";
                                                
                                                let bgClass = "bg-green-200 hover:bg-green-300 cursor-pointer";
                                                if(rowHasBlockedCells && estado === "LIBRE") bgClass = "bg-green-200 cursor-not-allowed";
                                                if (estado === "OCUPADA") bgClass = "bg-red-300 cursor-not-allowed";
                                                if (estado === "RESERVADA") bgClass = "bg-yellow-200 cursor-not-allowed";
                                                
                                                const isSelected = seleccion?.idHabitacion === idHab;

                                                return (
                                                    <td 
                                                        key={dia} 
                                                        className={`p-3 border ${bgClass} ${isSelected ? 'ring-2 ring-blue-600' : ''}`}
                                                        title={estado}
                                                        onClick={() => celda && seleccionarCelda(celda, rowHasBlockedCells)}
                                                    />
                                                )
                                            })}
                                        </tr>
                                    )
                                })}
                            </tbody>
                        </table>
                    </div>
                )}

                {/* Leyenda dentro del cuadro */}
                <div className="flex flex-wrap justify-center gap-x-6 gap-y-2 mt-4 p-4 border-t border-gray-200">
                    {/* Libre - Verde */}
                    <div className="flex items-center space-x-2">
                        <span className="w-4 h-4 rounded-full bg-green-200 border border-green-600"></span>
                        <span className="text-sm text-gray-700">Libre</span>
                    </div>

                    {/* Ocupada - Rojo */}
                    <div className="flex items-center space-x-2">
                        <span className="w-4 h-4 rounded-full bg-red-300 border border-red-600"></span>
                        <span className="text-sm text-gray-700">Ocupada</span>
                    </div>

                    {/* Reservada - Amarillo */}
                    <div className="flex items-center space-x-2">
                        <span className="w-4 h-4 rounded-full bg-yellow-200 border border-yellow-600"></span>
                        <span className="text-sm text-gray-700">Reservada</span>
                    </div>

                    {/* Fuera de Servicio - Gris */}
                    <div className="flex items-center space-x-2">
                        <span className="w-4 h-4 rounded-full bg-gray-300 border border-gray-600"></span>
                        <span className="text-sm text-gray-700">Fuera de Servicio </span>
                    </div>

                     {/* Seleccionada - Azul */}
                    <div className="flex items-center space-x-2">
                        <span className="w-4 h-4 rounded-full bg-white-200 border border-blue-600"></span>
                        <span className="text-sm text-gray-700">Seleccionada</span>
                    </div>
                </div>
                
                <div className="mt-0 flex justify-between items-center pt-4 w-full">
                    {/* Botón cancelar siempre visible */}
                    <button
                        type="button"
                        onClick={() => router.back()}
                        className="px-6 py-2 bg-gray-300 text-gray-800 font-bold rounded hover:bg-gray-400 transition-colors"
                    >
                        Cancelar
                    </button>

                    {/* Botón Siguiente: Verificar */}
                    {seleccion && (
                        <button onClick={() => setPaso(2)} className="bg-blue-600 text-white px-6 py-2 rounded font-bold hover:bg-blue-700 transition-colors">
                            Siguiente: Verificación
                        </button>
                    )}
                </div>
            </div>
        )}

        {paso === 2 && seleccion && (
            <div className="bg-white shadow rounded-lg p-6 border max-w-3xl mx-auto">
                <h2 className="text-xl font-bold mb-6 text-center">Verificar Reserva</h2>
                
                <div className="bg-gray-50 p-4 rounded border mb-6">
                    <div className="grid grid-cols-2 gap-4">
                        <div className="font-bold text-gray-600">Habitación:</div>
                        <div>{seleccion.idHabitacion}</div>

                        <div className="font-bold text-gray-600">Ingreso:</div>
                        <div>{seleccion.fechaInicio}, 12:00hs</div>

                        <div className="font-bold text-gray-600">Egreso:</div>
                        <div>{seleccion.fechaFin}, 10:00hs</div>
                        
                        <div className="font-bold text-gray-600">Noches:</div>
                        <div>{seleccion.cantNoches}</div>
                    </div>
                </div>

                <div className="flex justify-between items-center">
                    {/* Rechazar vuelve al paso 1 */}
                    <button onClick={handleRechazarVerificacion} className="bg-gray-300 text-gray-800 px-6 py-2 rounded font-bold hover:bg-gray-400">
                        Rechazar (Volver)
                    </button>

                    <button onClick={handleAceptarVerificacion} className="bg-blue-600 text-white px-6 py-2 rounded font-bold hover:bg-blue-700">
                        Aceptar
                    </button>
                </div>
            </div>
        )}

        {paso === 3 && (
            <div className="bg-white shadow rounded-lg p-6 border max-w-2xl mx-auto">
                <h2 className="text-xl font-bold mb-6 text-center border-b pb-2">Reserva a nombre de:</h2>
                
                <div className="space-y-4">
                    <div>
                        <label className="block text-gray-700 font-bold mb-1">DNI (*)</label>
                        <input 
                            type="number" 
                            className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none"
                            value={huesped.dni}
                            onChange={e => setHuesped({...huesped, dni: e.target.value})}
                        />
                    </div>

                    <div>
                        <label className="block text-gray-700 font-bold mb-1">Apellido (*)</label>
                        <input 
                            type="text" 
                            className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none"
                            value={huesped.apellido}
                            onChange={e => setHuesped({...huesped, apellido: e.target.value.toUpperCase()})}
                        />
                    </div>

                    <div>
                        <label className="block text-gray-700 font-bold mb-1">Nombre (*)</label>
                        <input 
                            type="text" 
                            className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none"
                            value={huesped.nombre}
                            onChange={e => setHuesped({...huesped, nombre: e.target.value})}
                        />
                    </div>

                    <div>
                        <label className="block text-gray-700 font-bold mb-1">Teléfono (*)</label>
                        <input 
                            type="text" 
                            className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none"
                            value={huesped.telefono}
                            onChange={e => setHuesped({...huesped, telefono: e.target.value})}
                        />
                    </div>
                </div>

                <div className="flex justify-between items-center mt-8 pt-4 border-t">
                    <button onClick={() => setPaso(2)} className="text-gray-500 font-bold hover:text-gray-700">
                        ← Volver
                    </button>

                    <button 
                        onClick={finalizarReserva} 
                        disabled={loading}
                        className="bg-green-600 text-white px-8 py-2 rounded font-bold hover:bg-green-700 shadow-lg transform hover:-translate-y-0.5 transition-all"
                    >
                        {loading ? "Registrando..." : "Confirmar Reserva"}
                    </button>
                </div>
            </div>
        )}

        </div>
    );
}