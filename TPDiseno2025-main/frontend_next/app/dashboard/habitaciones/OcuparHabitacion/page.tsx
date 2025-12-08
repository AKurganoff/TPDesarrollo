"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Swal from 'sweetalert2';
import Link from "next/link";

// --- TIPOS ---
interface CeldaCalendario {
    fecha: string;
    idHabitacion: number;
    estado: "LIBRE" | "OCUPADA" | "RESERVADA";
    idReserva?: number | null;
}

interface TipoHabitacion {
    idTipo: number;
    nombreTipo: string;
    precioNoche: number;
}

interface Huesped {
    dni: number;
    nombre: string;
    apellido: string;
    telefono?: string;
    tipoDni?: string; // Agregado para el form
}

export default function OcuparHabitacionPage() {
    const router = useRouter();
    const [paso, setPaso] = useState<1 | 2>(1);
    const [loading, setLoading] = useState(false);

    // --- ESTADOS PASO 1 (GRILLA) ---
    const [fechaDesde, setFechaDesde] = useState("");
    const [fechaHasta, setFechaHasta] = useState("");
    const [disponibilidad, setDisponibilidad] = useState<CeldaCalendario[]>([]);
    const [tiposHabitacion, setTiposHabitacion] = useState<TipoHabitacion[]>([]);
    const [tipoSeleccionado, setTipoSeleccionado] = useState<string>("");

    const [seleccion, setSeleccion] = useState<{
        idHabitacion: number;
        fechaInicio: string;
        fechaFin: string;
        idReservaPrevia: number | null;
    } | null>(null);

    // --- ESTADOS PASO 2 (BÚSQUEDA AVANZADA HUÉSPEDES) ---
    // Filtros del formulario nuevo
    const [filtrosBusqueda, setFiltrosBusqueda] = useState({
        apellido: "",
        nombre: "",
        tipoDni: "DNI",
        dni: ""
    });

    // Resultados encontrados (la tabla intermedia)
    const [resultadosBusqueda, setResultadosBusqueda] = useState<Huesped[]>([]);
    
    // Lista FINAL de huéspedes seleccionados para la ocupación
    const [listaHuespedes, setListaHuespedes] = useState<Huesped[]>([]); 

    // Configuración Toast
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true
    });

        const seleccionarCelda = (celda: CeldaCalendario, rowHasBlockedCells: boolean) => {
        if (celda.estado !== "LIBRE" && celda.estado !== "RESERVADA" || rowHasBlockedCells) {
            Toast.fire({
                icon: "error",
                title: "La habitación seleccionada no está disponible."
            });
            return;
        }

        setSeleccion({
            idHabitacion: celda.idHabitacion,
            fechaInicio: fechaDesde,
            fechaFin: fechaHasta,
            idReservaPrevia: null
        });
    };

    // Validación visual de fechas
    const errorFechas = fechaDesde && fechaHasta && fechaHasta < fechaDesde;

    useEffect(() => {
        const fetchTipos = async () => {
            try {
                const res = await fetch("http://localhost:8081/tiposhabitacion");
                if (res.ok) setTiposHabitacion(await res.json());
            } catch (error) { console.error(error); }
        };
        fetchTipos();
    }, []);

    // --- FUNCIONES PASO 1: BUSCAR Y SELECCIONAR ---
    const buscarDisponibilidad = async () => {
        if (!fechaDesde || !fechaHasta) {
            Toast.fire({ icon: "warning", title: "Seleccione fechas" });
            return;
        }
        setLoading(true);
        try {
            let url = `http://localhost:8081/reservas/disponibilidad?fechaInicio=${fechaDesde}&fechaFin=${fechaHasta}`;
            if (tipoSeleccionado) url += `&idTipo=${tipoSeleccionado}`;
            
            const res = await fetch(url);
            if (res.ok) setDisponibilidad(await res.json());
        } catch (e) {
            Toast.fire({ icon: "error", title: "Error de conexión" });
        } finally { setLoading(false); }
    };

    const handleCellClick = async (celda: CeldaCalendario, rowHasBlockedCells: boolean) => {
        if ((celda.estado === "OCUPADA" || rowHasBlockedCells)&& celda.estado !== "RESERVADA"){
            Swal.fire({ icon: "error", title: "Habitación Ocupada", text: "No se puede seleccionar." });
            return;
        }

        const idReservaDetectada = celda.idReserva ? celda.idReserva : null;

        const nuevaSeleccion = {
            idHabitacion: celda.idHabitacion,
            fechaInicio: fechaDesde,
            fechaFin: fechaHasta,
            idReservaPrevia: idReservaDetectada,
        };

        if (celda.estado === "RESERVADA") {
            const result = await Swal.fire({
                title: 'Habitación Reservada',
                text: "¿Desea realizar el Check-in sobre esta reserva?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'OCUPAR IGUAL',
                cancelButtonText: 'VOLVER',
                confirmButtonColor: '#d33',
            });
            if (!result.isConfirmed) return;
        }

        setSeleccion(nuevaSeleccion);
        setPaso(2); 
    };

    // --- FUNCIONES PASO 2: GESTIÓN HUÉSPEDES ---

    // Manejo de inputs del formulario de búsqueda
    const handleFiltroChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setFiltrosBusqueda({
            ...filtrosBusqueda,
            [e.target.name]: e.target.value
        });
    };

    // busqueda de huespedes
    const buscarHuespedes = async () => {
        setLoading(true);
        setResultadosBusqueda([]); // Limpiar tabla anterior
        try {
            let url = "";
            
            // CASO 1: Si hay DNI, buscamos exacto (Prioridad Alta)
            if (filtrosBusqueda.dni) {
                url = `http://localhost:8081/huespedes/getByDni?dni=${filtrosBusqueda.dni}`;
            } 
            // CASO 2: Si hay Nombre, buscamos por coincidencia
            else if (filtrosBusqueda.nombre) {
                url = `http://localhost:8081/huespedes/nombre?nombre=${filtrosBusqueda.nombre}`;
            } 
            // CASO 3: (NUEVO) Si NO hay nada escrito -> Traer TODOS
            else if (!filtrosBusqueda.apellido && !filtrosBusqueda.dni) {
                // Usamos el endpoint raíz que definiste en el Controller (getAllHuespedes)
                url = "http://localhost:8081/huespedes/";
            }
            // CASO 4: Si escribió algo en campos que no tienen búsqueda específica (ej: solo apellido)
            // Por ahora, asumimos traer todos o podrías implementar un endpoint específico de apellido
            else {
                url = "http://localhost:8081/huespedes/";
            }

            const res = await fetch(url);
            
            if (res.ok) {
                const data = await res.json();
                // Normalizamos: Si devuelve 1 objeto, lo hacemos array. Si es array, lo dejamos.
                const resultados = Array.isArray(data) ? data : [data];
                
                if (resultados.length === 0) {
                    Toast.fire({ icon: "info", title: "No se encontraron resultados" });
                }
                setResultadosBusqueda(resultados);
            } else {
                if (res.status === 404) {
                    // Si buscamos uno específico y no está
                    Swal.fire("No encontrado", "No existe huésped con esos datos.", "info");
                } else {
                    Toast.fire({ icon: "error", title: "Error al buscar" });
                }
            }
        } catch (e) {
            console.error(e);
            Toast.fire({ icon: "error", title: "Error de conexión" });
        } finally {
            setLoading(false);
        }
    };

    const agregarHuespedALista = (huesped: Huesped) => {
        // Verificar duplicados
        if (listaHuespedes.some(h => h.dni === huesped.dni)) {
            Toast.fire({ icon: "warning", title: "El huésped ya está en la lista" });
            return;
        }
        setListaHuespedes([...listaHuespedes, huesped]);
        Toast.fire({ icon: "success", title: "Agregado" });
    };

    const quitarHuesped = (dni: number) => {
        setListaHuespedes(listaHuespedes.filter(h => h.dni !== dni));
    };

    // --- FINALIZAR OCUPACIÓN ---
    const confirmarOcupacion = async () => {
        if (!seleccion || listaHuespedes.length === 0) return;

        setLoading(true);
        try {
            const payload = {
                habitaciones: [{ idHabitacion: seleccion.idHabitacion, idTipo: 1, nochesDescuento: 0, estado: "OCUPADA" }],
                huespedes: listaHuespedes.map(h => ({ 
                    dni: h.dni, nombre: h.nombre, apellido: h.apellido, tipoDni: h.tipoDni || "DNI"
                })),
                fechaInicio: seleccion.fechaInicio,
                fechaFin: seleccion.fechaFin,
                idReservaPrevia: seleccion.idReservaPrevia
            };

            const res = await fetch("http://localhost:8081/estadias/ocupar", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });

            if (res.ok) {
                await Swal.fire({
                    title: 'CONFIRMACIÓN',
                    html: `
                        <div class="text-left">
                            <p class="text-green-600 font-bold mb-2">✔ Habitación ocupada correctamente</p>
                            <p><strong>Titular:</strong> ${listaHuespedes[0].apellido}, ${listaHuespedes[0].nombre}</p>
                            <p class="text-sm text-gray-500 mt-2">Se han registrado ${listaHuespedes.length} persona(s).</p>
                        </div>
                    `,
                    icon: 'success',
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: 'Seguir cargando',
                    denyButtonText: 'Cargar otra hab.',
                    cancelButtonText: 'Salir'
                }).then((result) => {
                    if (result.isConfirmed) {
                        setListaHuespedes([]); // Limpiar lista pero seguir en paso 2? O volver a 1?
                        setResultadosBusqueda([]);
                        // Depende de tu flujo, aquí asumimos reset parcial
                    } else if (result.isDenied) {
                        setSeleccion(null);
                        setListaHuespedes([]);
                        setResultadosBusqueda([]);
                        setPaso(1);
                        buscarDisponibilidad();
                    } else {
                        router.push("/dashboard/habitaciones");
                    }
                });
            } else {
                const txt = await res.text();
                Swal.fire("Error", txt, "error");
            }
        } catch (e) {
            Swal.fire("Error", "Fallo de conexión", "error");
        } finally {
            setLoading(false);
        }
    };

    // --- RENDERIZADO ---
    const getDias = () => Array.from(new Set(disponibilidad.map(c => c.fecha))).sort();
    const getHabitaciones = () => Array.from(new Set(disponibilidad.map(c => c.idHabitacion))).sort((a, b) => a - b);

    return (
        <div className="p-3 max-w-6xl mx-auto font-sans text-gray-800">
            
                {/* HEADER */}
            <div className="mb-8 flex justify-center items-center bg-gray-100 p-0.5 rounded-lg space-x-50">
                <div className={`font-bold ${paso === 1 ? 'text-blue-600' : 'text-gray-400'}`}>1. Selección</div>
                <div className="text-gray-400">→</div>
                <div className={`font-bold ${paso === 2 ? 'text-blue-600' : 'text-gray-400'}`}>2. Verificación</div>
            </div>

            {/* PASO 1: SELECCIÓN DE HABITACIÓN */}
            {paso === 1 && (
                <div className="bg-white shadow rounded-lg p-6 border">                   
                    <div className="flex gap-4 mb-6 items-end flex-wrap">
                        <div>
                            <label className="block text-sm font-bold text-gray-600">Desde</label>
                            <input type="date" className="border p-2 rounded" value={fechaDesde} onChange={e => setFechaDesde(e.target.value)} />
                        </div>
                        <div>
                            <label className="block text-sm font-bold text-gray-600">Hasta</label>
                            <input type="date" 
                                className={`border p-2 rounded ${errorFechas ? 'border-red-500 bg-red-50' : ''}`} 
                                value={fechaHasta} 
                                onChange={e => setFechaHasta(e.target.value)} 
                            />
                            {errorFechas && (
                                <p className="text-red-500 text-xs mt-1 absolute w-70">
                                    La fecha de fin debe ser mayor a la fecha de inicio
                                </p>
                            )}  
                        </div>
                        <div>
                            <label className="block text-sm font-bold text-gray-600">Tipo</label>
                            <select className="border p-2 rounded w-48 bg-white" value={tipoSeleccionado} onChange={e => setTipoSeleccionado(e.target.value)}>
                                <option value="">Todas</option>
                                {tiposHabitacion.map(t => <option key={t.idTipo} value={t.idTipo}>{t.nombreTipo}</option>)}
                            </select>
                        </div>
                        <button
                            onClick={buscarDisponibilidad} 
                            className={`px-6 py-2 rounded font-bold text-white ${
                                loading || errorFechas 
                                ? 'bg-gray-400 cursor-not-allowed' 
                                : 'bg-blue-600 hover:bg-blue-700'
                            }`}
                            disabled={loading || !!errorFechas}>
                            {loading ? "Cargando..." : "Buscar"}
                        </button>
                    </div>

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
                                                    
                                                    let color = "bg-green-200 hover:bg-green-300 cursor-pointer";
                                                    if(rowHasBlockedCells && estado === "LIBRE") color = "bg-green-200 cursor-not-allowed";
                                                    if (estado === "OCUPADA") color = "bg-red-300 cursor-not-allowed";
                                                    if (estado === "RESERVADA") color = "bg-yellow-200 hover:bg-yellow-300 cursor-pointer";

                                                    const isSelected = seleccion?.idHabitacion === idHab;

                                                    return (
                                                        <td key={dia} className={`p-3 border ${color}`} 
                                                        onClick={() => celda && handleCellClick(celda, rowHasBlockedCells)}>
                                                    </td>
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

            {/* PASO 2: BÚSQUEDA AVANZADA (ESTILO MOCKUP) */}
            {paso === 2 && seleccion && (
                <div className="bg-white shadow-lg p-8 rounded border border-gray-200">
                    <div className="flex justify-between items-center mb-6 border-b pb-4">
                        <h2 className="text-2xl font-bold text-gray-800">2. Búsqueda de Acompañantes</h2>
                        <div className="text-right">
                            <p className="font-bold text-blue-600">Habitación {seleccion.idHabitacion}</p>
                            <p className="text-sm text-gray-500">{seleccion.fechaInicio} ➔ {seleccion.fechaFin}</p>
                        </div>
                    </div>

                    {/* --- FORMULARIO DE FILTROS --- */}
                    <div className="bg-gray-50 p-6 rounded-lg mb-8 border border-gray-200">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-4">
                            <div>
                                <label className="block text-sm font-bold text-gray-600 mb-1">Apellido</label>
                                <input 
                                    name="apellido"
                                    type="text" 
                                    className="border p-2 rounded w-full focus:ring-2 focus:ring-blue-400 outline-none" 
                                    placeholder="Ej: Pérez"
                                    value={filtrosBusqueda.apellido}
                                    onChange={handleFiltroChange}
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-bold text-gray-600 mb-1">Nombre</label>
                                <input 
                                    name="nombre"
                                    type="text" 
                                    className="border p-2 rounded w-full focus:ring-2 focus:ring-blue-400 outline-none" 
                                    placeholder="Ej: Juan"
                                    value={filtrosBusqueda.nombre}
                                    onChange={handleFiltroChange}
                                />
                            </div>
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 items-end">
                            <div>
                                <label className="block text-sm font-bold text-gray-600 mb-1">Documento</label>
                                <div className="flex">
                                    <select 
                                        name="tipoDni"
                                        className="border p-2 rounded-l bg-white border-r-0 focus:outline-none"
                                        value={filtrosBusqueda.tipoDni}
                                        onChange={handleFiltroChange}
                                    >
                                        <option value="DNI">DNI</option>
                                        <option value="PASAPORTE">PAS</option>
                                    </select>
                                    <input 
                                        name="dni"
                                        type="number" 
                                        className="border p-2 rounded-r w-full focus:ring-2 focus:ring-blue-400 outline-none" 
                                        placeholder="Número"
                                        value={filtrosBusqueda.dni}
                                        onChange={handleFiltroChange}
                                    />
                                </div>
                            </div>
     
                            <div>
                            </div>

                            <button 
                                onClick={buscarHuespedes} 
                                className="bg-gray-200 text-gray-800 border border-gray-300 px-6 py-2 rounded font-bold hover:bg-gray-300 w-full transition-colors"
                            >
                                Buscar
                            </button>
                        </div>
                    </div>

                    {/* --- TABLA DE RESULTADOS (SI EXISTEN) --- */}
                    {resultadosBusqueda.length > 0 && (
                        <div className="mb-8 animate-fade-in-up">
                            <h3 className="font-bold text-gray-700 mb-2">Resultados de la búsqueda:</h3>
                            <div className="overflow-hidden border rounded-lg">
                                <table className="w-full text-left bg-white">
                                    <thead className="bg-gray-100 text-gray-600 text-sm uppercase">
                                        <tr>
                                            <th className="p-3">Apellido</th>
                                            <th className="p-3">Nombre</th>
                                            <th className="p-3">Documento</th>
                                            <th className="p-3 text-center">Acción</th>
                                        </tr>
                                    </thead>
                                    <tbody className="divide-y divide-gray-200">
                                        {resultadosBusqueda.map((h) => (
                                            <tr key={h.dni} className="hover:bg-blue-50">
                                                <td className="p-3 font-semibold">{h.apellido}</td>
                                                <td className="p-3">{h.nombre}</td>
                                                <td className="p-3">{h.dni}</td>
                                                <td className="p-3 text-center">
                                                    <button 
                                                        onClick={() => agregarHuespedALista(h)}
                                                        className="bg-blue-600 text-white text-xs px-3 py-1 rounded hover:bg-blue-700"
                                                    >
                                                        + Agregar
                                                    </button>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    )}

                    {/* --- TABLA FINAL: HUÉSPEDES A REGISTRAR --- */}
                    <div className="mb-8">
                        <h3 className="font-bold text-gray-700 mb-2 flex justify-between items-center">
                            <span>Huéspedes a registrar:</span>
                            <span className="text-xs font-normal bg-blue-100 text-blue-800 px-2 py-1 rounded-full">Total: {listaHuespedes.length}</span>
                        </h3>
                        <table className="w-full text-left border-collapse border shadow-sm rounded-lg overflow-hidden">
                            <thead className="bg-blue-50 text-blue-800">
                                <tr>
                                    <th className="p-3 border border-blue-100">Apellido</th>
                                    <th className="p-3 border border-blue-100">Nombre</th>
                                    <th className="p-3 border border-blue-100">Documento</th>
                                    <th className="p-3 border border-blue-100 text-center">Quitar</th>
                                </tr>
                            </thead>
                            <tbody>
                                {listaHuespedes.length === 0 ? (
                                    <tr><td colSpan={4} className="p-6 text-center text-gray-400 italic">Aún no has agregado huéspedes. Usa el buscador arriba.</td></tr>
                                ) : (
                                    listaHuespedes.map((h, i) => (
                                        <tr key={h.dni} className="hover:bg-gray-50">
                                            <td className="p-3 border">{h.apellido}</td>
                                            <td className="p-3 border">{h.nombre}</td>
                                            <td className="p-3 border">{h.dni}</td>
                                            <td className="p-3 border text-center">
                                                <button onClick={() => quitarHuesped(h.dni)} className="text-red-500 hover:text-red-700 font-bold px-2">
                                                    ✕
                                                </button>
                                                {i === 0 && <span className="block text-[10px] text-blue-600 font-bold mt-1">(Titular)</span>}
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>

                    {/* --- BOTONES DE ACCIÓN FINAL --- */}
                    <div className="flex justify-between pt-4 border-t">
                        <button onClick={() => setPaso(1)} 
                        className="px-6 py-2 bg-gray-300 text-gray-800 font-bold rounded hover:bg-gray-400 transition-colors">
                        ← Volver
                        </button>
                        
                        <div className="flex gap-4">
                            <Link href="/dashboard/huesped/altaHuesped" target="_blank">
                                <button className="border border-blue-600 text-blue-600 px-6 py-2 rounded font-bold hover:text-white hover:bg-blue-600 transition">
                                    Nuevo Huésped
                                </button>
                            </Link>
                            <button 
                                onClick={confirmarOcupacion} 
                                disabled={listaHuespedes.length === 0}
                                className={`px-8 py-2 rounded font-bold text-white transition shadow-lg
                                    ${listaHuespedes.length === 0 ? 'bg-gray-400 cursor-not-allowed' : 'bg-green-600 hover:bg-green-700 hover:-translate-y-0.5'}`}
                            >
                                Aceptar
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}