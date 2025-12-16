"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Swal from 'sweetalert2'; 

// TIPOS 

interface Reserva {
    idReserva: number,
    apellido: string,
    nombre: string,
    numeroHabitacion: number,
    tipoHabitacion: TipoHabitacion,
    fechaInicio: string,
    fechaFin: string
}

interface TipoHabitacion {
    idTipo: number;
    nombreTipo: string;
    precioNoche: number;
}

export default function CancelarReservasPage() {
    const router = useRouter();

    const [paso, setPaso] = useState<1 | 2 | 3>(1);
    const [loading, setLoading] = useState(false);

    const [idReserva, setIdReserva] = useState<number>(0);
    const [selecciones, setSelecciones] = useState<number[]>([]);

    const [apellido, setApellido] = useState("");
    const [nombre, setNombre] = useState("");
    const [datosReservas, setDatosReservas] = useState<Reserva[]>([]);
    
    const errorApellido = apellido == "";
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

    const toggleSeleccion = (id: number) => {
        setSelecciones(prev =>
            prev.includes(id)
            ? prev.filter(x => x !== id)
            : [...prev, id]
        );
    };

    const seleccionarTodas = () => {
        setSelecciones(datosReservas.map(r => r.idReserva));
    };

    const limpiarSeleccion = () => {
        setSelecciones([]);
    };

    const volverAtras = async () => {
        setSelecciones([]);
        setPaso(2);
    }

    const cancelarReservas = async () => {
        if(selecciones.length = 0) {
            Toast.fire({
                icon: "warning",
                title: "Por favor seleccione una reserva."
            });
            return;
        }
        setLoading(true);
        try {
            const resultados = await Promise.allSettled(
                selecciones.map(id =>
                    fetch(`http://localhost:8081/reservas/${id}`, { method: "DELETE" })
                )
            );

            const okIds: number[] = [];
            const failIds: number[] = [];

            resultados.forEach((res, i) => {
                const id = selecciones[i];
                if (res.status === "fulfilled" && res.value.ok) okIds.push(id);
                else failIds.push(id);
            });

            if (okIds.length > 0) {
                Toast.fire({
                    icon: "success",
                    title: `Reservas canceladas: ${okIds.length}`
                });
                await Swal.fire({
                    icon: "success",
                    title: "Reservas canceladas",
                    text: "Presione una tecla para continuar",
                    confirmButtonText: "Continuar",
                    allowOutsideClick: false,
                    allowEscapeKey: true
                });
                router.back()
            }

            if (failIds.length > 0) {
                Swal.fire(
                    "Atención",
                    `No se pudieron cancelar ${failIds.length} reservas (IDs: ${failIds.join(", ")}).`,
                    "warning"
                );
            }
        } catch (e) {
            console.error(e);
            Swal.fire("Error", "No se pudo cancelar las reservas.", "error");
        } finally {
            setLoading(false);
        }
    }

    const buscarReserva = async () => {
        if(errorApellido) {
            Toast.fire({
                icon: "warning",
                title: "El campo apellido no puede estar vacio."
            });
            return;
        }
        setLoading(true);
        try {
            let url = `http://localhost:8081/reservas/?apellido=${apellido}`;

            if (nombre != "") {
                url += `&nombre=${nombre}`;
            }
            const res = await fetch(url);
            if (!res.ok) throw new Error("Error al conectar con backend");
            const data = await res.json();
            if (data.length === 0) {
                Toast.fire({
                    icon: "warning",
                    title: "No existen reservas para los criterios de busqueda."
                });
                setApellido("");
                setNombre("");
                setPaso(1);
            }
            else {
                setDatosReservas(data);
                setPaso(2);
            }
        } catch (e) {
            console.error(e);
            Swal.fire("Error", "No se pudo conectar con el servidor.", "error");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="p-3 max-w-6xl mx-auto font-sans text-gray-800">
        
        {/* HEADER PASOS */}
        <div className="mb-8 flex justify-center items-center bg-gray-100 p-0.5 rounded-lg">
           
            <div className={`font-bold text-4xl 'text-blue-600' : 'text-gray-400'}`}>Cancelar Reserva</div>
            
        </div>

        {/* --- VISTA 1: GRILLA DE DISPONIBILIDAD --- */}
        { (
            <div className="bg-white shadow rounded-lg p-6 border">
                {paso === 1 && (
                    <div>
                        <div className="mb-2 text-sm text-gray-400">
                            Introduzca un apellido asociado a una o varias reservas.
                        </div>
                        <div className="flex gap-4 mb-6 items-end flex-wrap">
                            <div>
                                <label className="block text-sm font-bold text-gray-700">*Apellido</label>
                                <input type="text" 
                                        className="border p-2 rounded"
                                        value={apellido} 
                                        onChange={e => setApellido(e.target.value)} 
                                />
                            </div>
                            <div className="relative">
                                <label className="block text-sm font-bold text-gray-700">Nombre</label>
                                <input type="text" 
                                        className="border p-2 rounded"
                                        value={nombre} 
                                        onChange={e => setNombre(e.target.value)} 
                                />
                            </div>

                            <button
                                onClick={buscarReserva} 
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
                {paso === 2 && datosReservas.length > 0 && (
                    <div className="mb-8">
                        <div>Seleccione la reservas a cancelar:</div>
                        <div className="flex gap-2 mb-2 mt-4">
                            <button
                            type="button"
                            onClick={seleccionarTodas}
                            className="px-3 py-1 rounded text-sm text-white bg-gray-500 hover:bg-gray-400"
                            >
                            Seleccionar todas
                            </button>

                            <button
                            type="button"
                            onClick={limpiarSeleccion}
                            className="px-3 py-1 rounded text-sm text-white bg-gray-500 hover:bg-gray-400"
                            >
                            Limpiar
                            </button>
                        </div>

                        <div className="overflow-x-auto">
                            <table className="min-w-full border text-sm">
                            <thead>
                                <tr className="border-b">
                                <th className="p-2 w-10"></th>
                                <th className="p-2 text-left">Reserva</th>
                                <th className="p-2 text-left">Fecha Inicio</th>
                                <th className="p-2 text-left">Fecha Fin</th>
                                <th className="p-2 text-left">Habitación</th>
                                <th className="p-2 text-left">Tipo Habitación</th>
                                <th className="p-2 text-left">Huesped</th>
                                </tr>
                            </thead>

                            <tbody>
                                {datosReservas.map(r => {
                                const checked = selecciones.includes(r.idReserva);

                                return (
                                    <tr key={r.idReserva} className="border-b">
                                    <td className="p-2">
                                        <input
                                        type="checkbox"
                                        checked={checked}
                                        onChange={() => toggleSeleccion(r.idReserva)}
                                        />
                                    </td>

                                    <td className="p-2">#{r.idReserva}</td>
                                    <td className="p-2">{r.fechaInicio}</td>
                                    <td className="p-2">{r.fechaFin}</td>
                                    <td className="p-2">{r.numeroHabitacion}</td>
                                    <td className="p-2">{r.tipoHabitacion.nombreTipo}</td>
                                    <td className="p-2">
                                        {r.apellido} {r.nombre}
                                    </td>
                                    </tr>
                                );
                                })}
                            </tbody>
                            </table>
                        </div>
                        <button
                            onClick={() => {
                                setPaso(3);
                            }} 
                            className={`px-4 py-2 rounded font-bold text-white mt-4 ${
                                loading || selecciones.length < 0
                                ? 'bg-gray-400 cursor-not-allowed' 
                                : 'bg-red-600 hover:bg-red-700'
                            }`}
                            disabled={loading || selecciones.length < 0}
                        >
                            {loading ? "Cargando..." : "Cancelar Reservas Seleccionadas"}
                        </button>
                    </div>
                )}

                {paso === 3 && (
                    <div className="mb-8">
                        <div className="mb-2 font-bold">Deseas cancelar las siguientes reservas?</div>
                        <ul>
                            {datosReservas.filter(r => selecciones.includes(r.idReserva))
                                .map(r => (
                                    <li className="text-xs text-gray-500" key={r.idReserva}>
                                        #{r.idReserva} [{r.fechaInicio}]-[{r.fechaFin}] - H{r.numeroHabitacion} - {r.tipoHabitacion.nombreTipo} - {r.apellido} {r.nombre}
                                    </li>
                            ))}
                        </ul>
                        <button
                            onClick={cancelarReservas} 
                            className={`px-4 py-2 rounded font-bold text-white mt-4 mr-2 ${
                                loading
                                ? 'bg-gray-400 cursor-not-allowed' 
                                : 'bg-blue-600 hover:bg-blue-700'
                            }`}
                            disabled={loading}
                        >
                            {loading ? "Cargando..." : "Aceptar"}
                        </button>
                        <button
                            onClick={volverAtras}
                            className={`px-4 py-2 rounded font-bold text-white ${
                                loading
                                ? 'bg-gray-400 cursor-not-allowed' 
                                : 'bg-red-600 hover:bg-red-700'
                            }`}
                            disabled={loading}
                        >
                            {loading ? "Cargando..." : "Cancelar"}
                        </button>
                    </div>
                )}

                {/* Pie con botón cancelar a la derecha */}
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

        )}

        </div>
    );
}