// Ubicación: src/app/dashboard/habitaciones/page.tsx
import Link from "next/link";

export default function FacturarMenuPage() {
  return (
    <div className="p-10 max-w-5xl mx-auto">
        <h1 className="text-4xl font-bold text-blue-900 mb-10 text-center border-b pb-4">
            Administracion de Facturas
        </h1>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <Link 
                href="/dashboard/habitaciones/OcuparHabitacion"
                className="group block p-8 bg-white border border-gray-200 rounded-xl shadow-lg hover:shadow-2xl hover:border-indigo-500 transition-all transform hover:-translate-y-1"
            >
                
                <div className="h-16 w-16 bg-indigo-100 text-indigo-600 rounded-full flex items-center justify-center mb-4 group-hover:bg-indigo-600 group-hover:text-white transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 7.5h1.5m-1.5 3h1.5m-7.5 3h7.5m-7.5 3h7.5m3-9h3.375c.621 0 1.125.504 1.125 1.125V18a2.25 2.25 0 0 1-2.25 2.25M16.5 7.5V18a2.25 2.25 0 0 0 2.25 2.25M16.5 7.5V4.875c0-.621-.504-1.125-1.125-1.125H4.125C3.504 3.75 3 4.254 3 4.875V18a2.25 2.25 0 0 0 2.25 2.25h13.5M6 7.5h3v3H6v-3Z" />
                </svg>
                </div>
                <h2 className="text-2xl font-bold text-gray-800 mb-2">Facturar</h2>
                <p className="text-gray-500">
                    Gestionar la factura de un huésped.
                </p>
            </Link>
        </div>
    </div>
  );
}