// Ubicación: src/app/reservas/page.tsx
import Link from "next/link";

export default function ReservasMenuPage() {
  return (
    <div className="p-10 max-w-5xl mx-auto">
      <h1 className="text-4xl font-bold text-blue-900 mb-10 text-center border-b pb-4">
        Administración de Reservas
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        
        {/* Opción 1: Crear Reserva (El Caso de Uso Principal) */}
        <Link 
          href="/dashboard/reservas/crearReserva"
          className="group block p-8 bg-white border border-gray-200 rounded-xl shadow-lg hover:shadow-2xl hover:border-blue-500 transition-all transform hover:-translate-y-1"
        >
          <div className="h-16 w-16 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center mb-4 group-hover:bg-blue-600 group-hover:text-white transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-8 h-8">
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
          </div>
          <h2 className="text-2xl font-bold text-gray-800 mb-2">Crear Reserva</h2>
          <p className="text-gray-500">
            Iniciar el proceso de reserva para una o varias habitaciones, seleccionando fechas y datos del huésped.
          </p>
        </Link>

        {/* Opción 2: Consultar Estados (Grilla sola) */}
        <Link 
          href="/dashboard/reservas/estadosReserva" 
          className="group block p-8 bg-white border border-gray-200 rounded-xl shadow-lg hover:shadow-2xl hover:border-green-500 transition-all transform hover:-translate-y-1"
        >
          <div className="h-16 w-16 bg-green-100 text-green-600 rounded-full flex items-center justify-center mb-4 group-hover:bg-green-600 group-hover:text-white transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-8 h-8">
              <path strokeLinecap="round" strokeLinejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75m-18 0v-7.5A2.25 2.25 0 015.25 9h13.5A2.25 2.25 0 0121 11.25v7.5" />
            </svg>
          </div>
          <h2 className="text-2xl font-bold text-gray-800 mb-2">Ver Estados</h2>
          <p className="text-gray-500">
            Consultar la disponibilidad y el estado actual de las habitaciones en formato calendario.
          </p>
        </Link>

        {/* Opción 3: Eliminar/Gestionar */}
        <div className="group block p-8 bg-gray-50 border border-gray-200 rounded-xl shadow opacity-75 cursor-not-allowed">
          <div className="h-16 w-16 bg-red-100 text-red-600 rounded-full flex items-center justify-center mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-8 h-8">
              <path strokeLinecap="round" strokeLinejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
            </svg>
          </div>
          <h2 className="text-2xl font-bold text-gray-800 mb-2">Eliminar Reserva</h2>
          <p className="text-gray-500">
            Módulo en construcción.
          </p>
        </div>

      </div>
    </div>
  );
}