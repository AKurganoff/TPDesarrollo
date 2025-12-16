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

export default function CancelarReservaPage() {
    const router = useRouter();

    const [loading, setLoading] = useState(false);

    const [idReserva, setIdReserva] = useState<number>(0);

    const [apellido, setApellido] = useState("");
    const [nombre, setNombre] = useState("");
    const [datosReservas, setDatosReservas] = useState<Reserva[]>([]);
    
    const errorApellido = apellido == "";
    const errorId = idReserva <= 0;
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

    const cancelarReserva = async () => {
        if(errorId) {
            Toast.fire({
                icon: "warning",
                title: "Por favor seleccione una reserva."
            });
            return;
        }
        setLoading(true);
        try {
            const res = await fetch(
                `http://localhost:8081/reservas/${idReserva}`,
                {
                    method: "DELETE",
                }
            );

            if (!res.ok) throw new Error("Error al cancelar la reserva");

            Toast.fire({
                icon: "success",
                title: "Reserva cancelada correctamente."
            });

            setDatosReservas(prev => prev.filter(r => r.idReserva !== idReserva));

            setIdReserva(0);

        } catch (e) {
            console.error(e);
            Swal.fire("Error", "No se pudo cancelar la reserva.", "error");
        } finally {
            setLoading(false);
        }
    }

    const buscarReserva = async () => {
        if(errorApellido) {
            Toast.fire({
                icon: "warning",
                title: "Por favor especifique un apellido asociado a la reserva."
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
            setDatosReservas(data);
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
                            loading || errorApellido
                            ? 'bg-gray-400 cursor-not-allowed' 
                            : 'bg-blue-600 hover:bg-blue-700'
                        }`}
                        disabled={loading || errorApellido}
                    >
                        {loading ? "Cargando..." : "Buscar"}
                    </button>
                </div>
               
                {datosReservas.length > 0 && (
                    <div className="mb-8">
                        <div>Seleccione la reserva a cancelar:</div>
                        <div className="mb-2 mt-2">
                            <select
                                value={idReserva}
                                onChange={e => setIdReserva(Number(e.target.value))}
                                className="border rounded p-2 text-sm"
                                >
                                <option value="0">Seleccione una reserva</option>
                                {datosReservas.map(r => (
                                    <option key={r.idReserva} value={r.idReserva}>
                                        #{r.idReserva} [{r.fechaInicio}]-[{r.fechaFin}] - H{r.numeroHabitacion} - {r.apellido} {r.nombre}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <button
                            onClick={cancelarReserva} 
                            className={`px-4 py-2 rounded font-bold text-white ${
                                loading || errorId
                                ? 'bg-gray-400 cursor-not-allowed' 
                                : 'bg-red-600 hover:bg-red-700'
                            }`}
                            disabled={loading || errorId}
                        >
                            {loading ? "Cargando..." : "Cancelar Reserva"}
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
                        Volver Atras
                    </button>
                </div>
            </div>

        )}

        </div>
    );
}