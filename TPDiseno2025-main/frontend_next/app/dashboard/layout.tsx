import Sidebar from '../components/Sidebar';

export default function DashboardLayout({
  children, // Esto representa la página específica (huespedes, reservas, etc.)
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="flex h-screen bg-gray-100">
      {/* 1. Menú Lateral (Fijo) */}
      <Sidebar />

      {/* 2. Área de Contenido (Dinámico) */}
      <main className="flex-1 overflow-y-auto p-8 text-black">
        {children}
      </main>
    </div>
  );
}