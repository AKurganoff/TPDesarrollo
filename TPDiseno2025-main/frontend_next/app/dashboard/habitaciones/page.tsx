// Ubicación: src/app/dashboard/habitaciones/page.tsx
import Link from "next/link";

export default function HabitacionesMenuPage() {
  return (
    <div className="p-10 max-w-5xl mx-auto">
      <h1 className="text-4xl font-bold text-blue-900 mb-10 text-center border-b pb-4">
        Administración de Habitaciones
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <Link 
          href="/dashboard/habitaciones/OcuparHabitacion"
          className="group block p-8 bg-white border border-gray-200 rounded-xl shadow-lg hover:shadow-2xl hover:border-indigo-500 transition-all transform hover:-translate-y-1"
        >
          
          <div className="h-16 w-16 bg-indigo-100 text-indigo-600 rounded-full flex items-center justify-center mb-4 group-hover:bg-indigo-600 group-hover:text-white transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-9 h-9">
              <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 5.25a3 3 0 013 3m3 0a6 6 0 01-7.029 5.912c-.563-.097-1.159.026-1.563.43L10.5 17.25H8.25v2.25H6v2.25H2.25v-2.818c0-.597.237-1.17.659-1.591l6.499-6.499c.404-.404.527-1 .43-1.563A6 6 0 1121.75 8.25z" />
            </svg>
          </div>
          <h2 className="text-2xl font-bold text-gray-800 mb-2">Ocupar Habitación</h2>
          <p className="text-gray-500">
            Registrar ingreso inmediato de huéspedes (Walk-in) o confirmar una reserva existente (Check-in).
          </p>
        </Link>

        <Link 
          href="/dashboard/reservas/estadosReserva" 
          className="group block p-8 bg-white border border-gray-200 rounded-xl shadow-lg hover:shadow-2xl hover:border-teal-500 transition-all transform hover:-translate-y-1"
        >
          
          <div className="h-16 w-16 bg-teal-100 text-teal-600 rounded-full flex items-center justify-center mb-4 group-hover:bg-teal-600 group-hover:text-white transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-9 h-9">
              <path strokeLinecap="round" strokeLinejoin="round" d="M3.75 21h16.5M4.5 3h15M5.25 3v18m13.5-18v18M9 6.75h1.5M12 6.75h1.5m-3 3h1.5m0 0h1.5m-3 3h1.5m0 0h1.5M9 15.75h1.5m0 0h1.5" />
            </svg>
          </div>
          <h2 className="text-2xl font-bold text-gray-800 mb-2">Estado de Habitaciones</h2>
          <p className="text-gray-500">
            Visualizar la grilla de disponibilidad, consultar ocupación actual y características.
          </p>
        </Link>

      </div>
    </div>
  );
}