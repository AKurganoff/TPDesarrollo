'use client';

import Link from 'next/link';
import { useRouter, usePathname } from 'next/navigation';
import { useEffect, useState } from 'react';

export default function Sidebar() {
  const router = useRouter();
  const pathname = usePathname(); // Para saber en qué página estamos y pintarla de otro color
  const [usuario, setUsuario] = useState<any>(null);

  useEffect(() => {
    // Recuperar el usuario para mostrar su nombre
    const storedUser = localStorage.getItem('usuario');
    if (storedUser) {
      setUsuario(JSON.parse(storedUser));
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('usuario');
    router.push('/');
  };

  // Función auxiliar para los estilos de los links
  const getLinkClass = (path: string) => {
    const isActive = pathname === path;
    return `block py-2.5 px-4 rounded transition duration-200 ${
      isActive ? 'bg-blue-700 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white'
    }`;
  };

  return (
    <div className="bg-gray-900 text-white w-64 min-h-screen flex flex-col justify-between">
      
      {/* Parte Superior: Logo y Menú */}
      <div>
        <div className="p-6 text-2xl font-bold text-center border-b border-gray-700">
          Gestion Hotelera 🏨
        </div>

        <nav className="mt-6 px-4 space-y-2">
          <Link href="/dashboard" className={getLinkClass('/dashboard')}>
            🏠 Inicio
          </Link>
          
          <Link href="/dashboard/huesped" className={getLinkClass('/dashboard/huesped')}>
            👥 Huéspedes
          </Link>

          <Link href="/dashboard/reservas" className={getLinkClass('/dashboard/reservas')}>
            📅 Reservas
          </Link>

          <Link href="/dashboard/habitaciones" className={getLinkClass('/dashboard/habitaciones')}>
            🛏️ Habitaciones
          </Link>
        </nav>
      </div>

      {/* Parte Inferior: Usuario y Logout */}
      <div className="p-4 border-t border-gray-700">
        <button
          onClick={handleLogout}
          className="w-full bg-red-600 hover:bg-red-700 text-white font-bold py-2 px-4 rounded transition"
        >
          Cerrar Sesión
        </button>
      </div>
    </div>
  );
}