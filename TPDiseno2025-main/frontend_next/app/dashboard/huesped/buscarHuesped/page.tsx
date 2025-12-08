"use client";

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Swal from 'sweetalert2';

// Definimos la estructura de datos del huésped
interface Huesped {
  nombre: string;
  apellido: string;
  tipoDni: string;
  dni: number;
  email: string;
}

export default function BuscarHuespedPage() {
  const router = useRouter();

  // Estados del formulario
  const [filtros, setFiltros] = useState({
    apellido: '',
    nombre: '',
    tipoDni: 'DNI',
    dni: ''
  });

  // Estados de la búsqueda
  const [resultados, setResultados] = useState<Huesped[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [busquedaRealizada, setBusquedaRealizada] = useState(false); // Para ocultar la tabla al inicio
  const [seleccionado, setSeleccionado] = useState<number | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFiltros({ ...filtros, [e.target.name]: e.target.value });
  };

  const handleBuscar = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSeleccionado(null);
    setResultados([]); // Limpiamos resultados anteriores

    try {
      let url = 'http://localhost:8081/huespedes/';
      // Identificamos si se está buscando activamente por DNI o Nombre para manejar el modal
      const buscandoPorDni = filtros.dni && filtros.dni.trim() !== '';
      const buscandoPorNombre = filtros.nombre && filtros.nombre.trim() !== '';
      const buscandoPorApellido = filtros.apellido && filtros.apellido.trim() !== '';
      const buscandoPorTipoDoc = filtros.tipoDni && filtros.tipoDni.trim() !== '';
      const buscandoPorFiltro = buscandoPorDni || buscandoPorNombre || buscandoPorApellido;

      // Si ambos están vacíos, la URL queda como '/huespedes/' (Buscar Todos)
      if (buscandoPorDni) {
        url = `http://localhost:8081/huespedes/${filtros.tipoDni}/${filtros.dni}`;
      } else if (buscandoPorNombre) {
        url = `http://localhost:8081/huespedes/nombre?nombre=${filtros.nombre}`;
      } else if (buscandoPorApellido){
        url = `http://localhost:8081/huespedes/apellido?apellido=${filtros.apellido}`;
      } else if (buscandoPorTipoDoc){
        url = `http://localhost:8081/huespedes/buscarPorTipo/${filtros.tipoDni}`;
      }
      

      const response = await fetch(url);

      // MANEJO DE ERRORES 
      if (!response.ok) {
        // Si la búsqueda fue por DNI y el servidor responde con 404/error (Recurso no encontrado)
        if (buscandoPorDni) {
            const resultado = await Swal.fire({
                title: 'Huésped no encontrado',
                text: `El DNI ${filtros.dni} no figura en el sistema. ¿Desea registrarlo ahora?`,
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Sí, registrar',
                cancelButtonText: 'Cancelar'
            });

            if (resultado.isConfirmed) router.push("/dashboard/huesped/altaHuesped");
            setBusquedaRealizada(true);
            return;
        } else {
            // Error en otra búsqueda (Nombre o Buscar Todos) o error grave del servidor
            const errorText = await response.text();
            setError(`Error ${response.status}: ${errorText || 'Error desconocido del servidor.'}`);
            setBusquedaRealizada(true);
            return;
        }
      }

      // MANEJO DE RESULTADOS EXITOSOS (response.ok = true)
      const data = await response.json();
      // Aseguramos que data sea siempre una lista. Si es un objeto, lo ponemos en una lista.
      const lista = Array.isArray(data) ? data : (data ? [data] : []); 
      
      // Si la búsqueda fue exitosa, pero no se encontraron resultados
      if (lista.length === 0) {
        setBusquedaRealizada(true);

        // Si se usó un filtro (DNI o Nombre) y no hubo resultados, mostramos el modal de sugerencia de registro.
        if (buscandoPorFiltro) {
             const resultado = await Swal.fire({
                title: 'Huésped no encontrado',
                text: `No se encontraron huéspedes con el criterio de búsqueda. ¿Desea registrarlo ahora?`,
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Sí, registrar',
                cancelButtonText: 'Cancelar'
            });

            if (resultado.isConfirmed) router.push("/dashboard/huesped/altaHuesped");
        }
        setResultados([]); 
        return; 
      }

      // 3. Resultado OK
      setResultados(lista);
      setBusquedaRealizada(true); 

    } catch (err: any) {
      setError(err.message);
      setBusquedaRealizada(true);
    } finally {
      setLoading(false);
    }
  };

  const handleLimpiar = () => {
    setFiltros({ apellido: '', nombre: '', tipoDni: 'DNI', dni: '' });
    setBusquedaRealizada(false); // Ocultamos la tabla de nuevo
    setResultados([]);
    setError(null);
  };

  const handleSiguiente = () => {
    if (seleccionado) {
      console.log("Seleccionado DNI:", seleccionado);
      router.push(`/dashboard/huesped/modificarHuesped/${seleccionado}`);
    }
  };

  return (
    <div className="max-w-5xl mx-auto p-4">
      
      {/* TÍTULO  */}
      <h1 className="text-4xl font-extrabold text-center text-gray-800 mb-10 tracking-tight">
        Búsqueda de Huéspedes
      </h1>

      {/*  FORMULARIO */}
      <div className="bg-white p-8 rounded-xl shadow-lg border border-gray-100">
        <form onSubmit={handleBuscar} className="space-y-8">
          
          {/*  Apellido y Nombre */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div className="flex flex-col gap-2">
              <label className="font-bold text-gray-700">Apellido</label>
              <input
                type="text"
                name="apellido"
                value={filtros.apellido}
                onChange={handleChange}
                placeholder="Ej: Pérez"
                className="p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition text-gray-800 bg-gray-50"
              />
            </div>
            
            <div className="flex flex-col gap-2">
              <label className="font-bold text-gray-700">Nombre</label>
              <input
                type="text"
                name="nombre"
                value={filtros.nombre}
                onChange={handleChange}
                placeholder="Ej: Juan"
                className="p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition text-gray-800 bg-gray-50"
              />
            </div>
          </div>

          {/* Documento */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div className="flex flex-col gap-2">
              <label className="font-bold text-gray-700">Tipo Documento</label>
              <select
                name="tipoDni"
                value={filtros.tipoDni}
                onChange={handleChange}
                className="p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none bg-gray-50 text-gray-800"
              >
                <option value="DNI">DNI</option>
                <option value="PASAPORTE">Pasaporte</option>
                <option value="LC">LC</option>
              </select>
            </div>

            <div className="flex flex-col gap-2">
              <label className="font-bold text-gray-700">Número</label>
              <input
                type="number"
                name="dni"
                value={filtros.dni}
                onChange={handleChange}
                placeholder="Ej: 12345678"
                className="p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition text-gray-800 bg-gray-50"
              />
            </div>
          </div>

          {/* Botones de Acción */}
          <div className="flex justify-end gap-4 pt-4 border-t border-gray-100">
            <button
              type="button"
              onClick={handleLimpiar}
              className="px-6 py-2.5 text-gray-700 bg-gray-200 hover:bg-gray-300 rounded-lg font-semibold transition"
            >
              Cancelar / Limpiar
            </button>
            <button
              type="submit"
              disabled={loading}
              className="px-8 py-2.5 text-white bg-blue-600 hover:bg-blue-700 rounded-lg font-semibold shadow-md transition disabled:opacity-50 flex items-center gap-2"
            >
              {loading ? (
                <span>Buscando...</span>
              ) : (
                <>
                  <span>Buscar</span>
                  <span>🔍</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>

      {/* RESULTADOS (Oculta hasta buscar) */}
      {busquedaRealizada && (
        <div className="mt-10 animate-fade-in-up">
          
          {error ? (
            <div className="p-6 bg-red-100 border-l-4 border-red-500 text-red-700 rounded shadow">
              <p className="font-bold">Error</p>
              <p>{error}</p>
            </div>
          ) : resultados.length === 0 ? (
            <div className="p-8 text-center text-gray-500 bg-white rounded-lg shadow border border-gray-200">
              No se encontraron huéspedes con esos criterios.
            </div>
          ) : (
            <div className="bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden">
              <div className="overflow-x-auto">
                <table className="w-full text-left border-collapse">
                  <thead className="bg-gray-100 text-gray-700 uppercase text-xs font-bold tracking-wider">
                    <tr>
                      <th className="p-4 border-b">Apellido</th>
                      <th className="p-4 border-b">Nombre</th>
                      <th className="p-4 border-b">Tipo Doc</th>
                      <th className="p-4 border-b">Nro Documento</th>
                      <th className="p-4 border-b text-center">Selección</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-200 text-sm text-gray-700">
                    {resultados.map((h) => (
                      <tr 
                        key={h.dni} 
                        className={`hover:bg-blue-50 transition cursor-pointer ${seleccionado === h.dni ? 'bg-blue-50' : ''}`}
                        onClick={() => setSeleccionado(h.dni)}
                      >
                        <td className="p-4 font-semibold">{h.apellido}</td>
                        <td className="p-4">{h.nombre}</td>
                        <td className="p-4">{h.tipoDni}</td>
                        <td className="p-4 font-mono">{h.dni}</td>
                        <td className="p-4 text-center">
                          <div className={`w-5 h-5 rounded-full border-2 mx-auto flex items-center justify-center transition
                            ${seleccionado === h.dni ? 'border-blue-600 bg-blue-600' : 'border-gray-400'}`}>
                            {seleccionado === h.dni && <div className="w-2 h-2 bg-white rounded-full"></div>}
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              {/* Botón Siguiente */}
              <div className="p-4 bg-gray-50 border-t border-gray-200 flex justify-end">
                <button
                  onClick={handleSiguiente}
                  disabled={!seleccionado}
                  className={`px-6 py-2 rounded-lg font-semibold shadow transition 
                    ${!seleccionado 
                      ? 'bg-gray-300 text-gray-500 cursor-not-allowed' 
                      : 'bg-green-600 text-white hover:bg-green-700'}`}
                >
                  Siguiente
                </button>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}