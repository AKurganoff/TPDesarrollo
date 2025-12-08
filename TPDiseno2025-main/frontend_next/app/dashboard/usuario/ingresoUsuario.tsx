'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function AuthComponent() {
  const router = useRouter();
  const [isLogin, setIsLogin] = useState(true);
  const [error, setError] = useState<string | null>(null);
  
  // Ahora el estado inicial solo tiene nombre y password
  const [formData, setFormData] = useState({
    nombre: '',
    password: ''
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    const endpoint = isLogin 
      ? 'http://localhost:8081/auth/login' 
      : 'http://localhost:8081/auth/register';

    try {
      const response = await fetch(endpoint, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            nombre_usuario: formData.nombre,
            psw: formData.password
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.mensaje || 'Error en la autenticación');
      }

      const data = await response.json();
      console.log('Éxito:', data);
      
      // Guardar usuario en localStorage
      localStorage.setItem('usuario', JSON.stringify(data));
      
      // Redirigir
      router.push('/dashboard'); 

    } catch (err: any) {
      setError(err.message);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-100">
      <div className="w-full max-w-md rounded-lg bg-white p-8 shadow-md">
        
        <h2 className="mb-6 text-center text-2xl font-bold text-gray-800">
          {isLogin ? 'Bienvenido' : 'Crear Cuenta'}
        </h2>

        {error && (
          <div className="mb-4 rounded bg-red-100 p-3 text-sm text-red-700">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          
          {/* NOMBRE */}
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Nombre de Usuario
            </label>
            <input
              type="text"
              name="nombre"
              value={formData.nombre}
              placeholder="Ingresa tu usuario"
              className="mt-1 w-full rounded border p-2 text-black focus:border-blue-500 focus:outline-none"
              onChange={handleChange}
              required
            />
          </div>

          {/* Campo PASSWORD */}
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Contraseña
            </label>
            <input
              type="password"
              name="password"
              value={formData.password}
              placeholder="******"
              className="mt-1 w-full rounded border p-2 text-black focus:border-blue-500 focus:outline-none"
              onChange={handleChange}
              required
            />
          </div>

          <button
            type="submit"
            className="w-full rounded bg-blue-600 py-2 text-white hover:bg-blue-700 transition font-semibold"
          >
            {isLogin ? 'Ingresar' : 'Registrarse'}
          </button>
        </form>

        <p className="mt-4 text-center text-sm text-gray-600">
          {isLogin ? '¿No tienes usuario?' : '¿Ya tienes usuario?'}
          <button
            onClick={() => {
                setIsLogin(!isLogin);
                setError(null); 
            }}
            className="ml-1 font-medium text-blue-600 hover:underline"
          >
            {isLogin ? 'Regístrate' : 'Inicia sesión'}
          </button>
        </p>

      </div>
    </div>
  );
}