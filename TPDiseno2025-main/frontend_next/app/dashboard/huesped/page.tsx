'use client'; // 👈 IMPORTANTE: Esto permite usar interactividad

import { useRouter } from 'next/navigation';

export default function HuespedMenuPage() {
  const router = useRouter();


  return (
    <div className="p-10 max-w-5xl mx-auto">
      <h1 className="text-4xl font-bold text-blue-900 mb-10 text-center border-b pb-4">Administración de Huéspedes</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
  
        {/* Opción 1: Buscar Huésped */}
        <div 
          onClick={() => router.push('/dashboard/huesped/buscarHuesped')}
          className="group cursor-pointer p-8 bg-white border border-gray-200 rounded-xl shadow-lg hover:shadow-2xl hover:border-blue-500 transition-all transform hover:-translate-y-1 flex flex-col items-start text-left"
        >
          <div className="h-16 w-16 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center mb-4 text-3xl font-semibold group-hover:bg-blue-600 group-hover:text-white transition-colors">
            S
          </div>
          <h2 className="text-2xl font-bold text-gray-800 mb-2">Buscar Huésped</h2>
          <p className="text-gray-500">
            Consulta el padrón y visualiza datos.
          </p>
        </div>

        {/* Opción 2: Nuevo Huésped (Alta) */}
        <div 
          onClick={() => router.push('/dashboard/huesped/altaHuesped')}
          className="group cursor-pointer p-8 bg-white border border-gray-200 rounded-xl shadow-lg hover:shadow-2xl hover:border-green-500 transition-all transform hover:-translate-y-1 flex flex-col items-start text-left"
        >
          <div className="h-16 w-16 bg-green-100 text-green-600 rounded-full flex items-center justify-center mb-4 text-3xl font-semibold group-hover:bg-green-600 group-hover:text-white transition-colors">
            R
          </div>
          <h2 className="text-2xl font-bold text-gray-800 mb-2">Nuevo Huésped</h2>
          <p className="text-gray-500">
            Registra un nuevo cliente.
          </p>
        </div>

        {/* Opción 3: Modificar Huésped */}
        <div 
          onClick={() => router.push('/dashboard/huesped/buscarHuesped')}
          className="group cursor-pointer p-8 bg-white border border-gray-200 rounded-xl shadow-lg hover:shadow-2xl hover:border-orange-500 transition-all transform hover:-translate-y-1 flex flex-col items-start text-left"
        >
          <div className="h-16 w-16 bg-orange-100 text-orange-600 rounded-full flex items-center justify-center mb-4 text-3xl font-semibold group-hover:bg-orange-600 group-hover:text-white transition-colors">
            M
          </div>
          <h2 className="text-2xl font-bold text-gray-800 mb-2">Modificar Huésped</h2>
          <p className="text-gray-500">
            Modifica o da de baja.
          </p>
        </div>

      </div>

    </div>
  );
}