"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Swal from 'sweetalert2'; 



// --- TIPOS ---
interface CeldaCalendario {
  fecha: string;
  idHabitacion: number;
  estado: "LIBRE" | "OCUPADA" | "RESERVADA";
}

interface TipoHabitacion {
    idTipo: number;
    nombreTipo: string;
    precioNoche: number;
}

export default function EstadosPage() {
    const router = useRouter();
    
    // --- ESTADOS ---
    const [paso, setPaso] = useState<1 | 2 | 3>(1);
    const [loading, setLoading] = useState(false);

    // Paso 1: Búsqueda y Selección
    const [fechaDesde, setFechaDesde] = useState("");
    const [fechaHasta, setFechaHasta] = useState("");
    const [disponibilidad, setDisponibilidad] = useState<CeldaCalendario[]>([]);
    const [tiposHabitacion, setTiposHabitacion] = useState<TipoHabitacion[]>([]);
    const [tipoSeleccionado, setTipoSeleccionado] = useState<string>(""); 
    


 
    // Validación visual de fechas
    const errorFechas = fechaDesde && fechaHasta && fechaHasta < fechaDesde;

    // --- CONFIGURACIÓN SWEETALERT (TOAST) ---
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

       
    }, []);

    // --- LOGICA PASO 1: MOSTRAR ESTADO HABITACIONES ---
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




    // --- RENDERIZADO ---
    const getDias = () => Array.from(new Set(disponibilidad.map(c => c.fecha))).sort();
    const getHabitaciones = () => Array.from(new Set(disponibilidad.map(c => c.idHabitacion))).sort((a,b) => a-b);

    return (
        <div className="p-3 max-w-6xl mx-auto font-sans text-gray-800">
        
        {/* HEADER PASOS */}
        <div className="mb-8 flex justify-center items-center bg-gray-100 p-0.5 rounded-lg">
           
            <div className={`font-bold text-4xl 'text-blue-600' : 'text-gray-400'}`}>Estado Habitaciones</div>
            
        </div>

        {/* --- VISTA 1: GRILLA DE DISPONIBILIDAD --- */}
        { (
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
                            <p className="text-red-500 text-xs mt-1 absolute w-78">
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
                        <table className="min-w-full text-center border-collapse">
                            <thead className="bg-gray-200 sticky top-0 z-30">
                                <tr>
                                    <th className="p-3 border sticky left-0 top-0 z-40 bg-gray-200 whitespace-nowrap">Habitación</th>
                                    {getDias().map(dia => (
                                        <th key={dia} className="p-3 border min-w-[100px] whitespace-nowrap">{dia}</th>
                                    ))}
                                </tr>
                            </thead>
                             <tbody>
                                {getHabitaciones().map(idHab => (
                                    <tr key={idHab}>
                                        <td className="p-3 border sticky left-0 z-20 font-bold bg-gray-50 min-w-[120px]">Hab {idHab}</td>
                                        {getDias().map(dia => {
                                            const celda = disponibilidad.find(c => c.idHabitacion === idHab && c.fecha === dia);
                                            const estado = celda ? celda.estado : "DESCONOCIDO";
                                            
                                            let bgClass = "bg-green-200"; 
                                            if(estado === "OCUPADA") bgClass = "bg-red-300";
                                            if(estado === "RESERVADA") bgClass = "bg-yellow-200";
                                            
                                            return (
                                                <td 
                                                    key={dia} 
                                                    className={`p-3 border ${bgClass} text-center font-semibold`}
                                                    title={estado}
                                                />
                                            )
                                        })}
                                    </tr>
                                ))}
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

                    {/* Pie con botón cancelar a la derecha */}
                    <div className="w-full flex justify-i mt-0">
                        <button
                            type="button"
                            onClick={() => router.back()}
                            className="px-6 py-2 bg-gray-300 text-gray-800 font-bold rounded hover:bg-gray-400 transition-colors"
                        >
                            Cancelar
                        </button>
                    </div>
                </div>

            </div>

        )}

        </div>
    );
}