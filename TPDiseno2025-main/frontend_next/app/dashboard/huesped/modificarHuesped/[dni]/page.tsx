"use client";

import React, { useState, useEffect, use } from "react"; 
import { useRouter } from "next/navigation";
import Swal from 'sweetalert2'; 

interface HuespedForm {
  dni: string;
  tipoDni: string;
  apellido: string;
  nombre: string;
  fechaNacimiento: string;
  email: string;
  telefono: string;
  ocupacion: string;
  posIva: string;
  cuil: string; // Agregado
  nacionalidad: string;
  calle: string;
  numero: string;
  departamento: string;
  piso: string;
  codPostal: string;
  localidad: string;
  provincia: string;
  pais: string;
}

export default function ModificarHuespedPage({ params }: { params: Promise<{ dni: string }> }) {
  const router = useRouter();
  
  const resolvedParams = use(params);
  const dniUrl = resolvedParams.dni; 

  const [loading, setLoading] = useState(true);
  const [guardando, setGuardando] = useState(false);
  const [errores, setErrores] = useState<Record<string, string>>({}); // Estado para bordes rojos
  
  const [form, setForm] = useState<HuespedForm>({
    dni: "", tipoDni: "DNI", apellido: "", nombre: "", fechaNacimiento: "",
    email: "", telefono: "", ocupacion: "", posIva: "Consumidor Final", 
    cuil: "", nacionalidad: "",
    calle: "", numero: "", departamento: "", piso: "", codPostal: "",
    localidad: "", provincia: "", pais: ""
  });

  // --- 1. CARGAR DATOS AL INICIAR ---
  useEffect(() => {
    const cargarHuesped = async () => {
      try {
        const res = await fetch(`http://localhost:8081/huespedes/getByDni?dni=${dniUrl}`);
        if (res.ok) {
          const data = await res.json();
          
          setForm({
            dni: data.dni.toString(),
            tipoDni: data.tipoDni,
            apellido: data.apellido,
            nombre: data.nombre,
            cuil: data.cuil || "", // Asumiendo que el backend trae CUIL
            fechaNacimiento: data.fechaNacimiento || "",
            email: data.email || "",
            telefono: data.telefono || "",
            ocupacion: data.ocupacion || "",
            posIva: data.posIva || "Consumidor Final",
            nacionalidad: data.nacionalidad || "",
            
            // Datos de Dirección
            calle: data.direccion?.calle || "",
            numero: data.direccion?.numero?.toString() || "",
            departamento: data.direccion?.departamento || "",
            piso: data.direccion?.piso || "",
            codPostal: data.direccion?.codPostal?.toString() || "",
            localidad: data.direccion?.localidad || "",
            provincia: data.direccion?.provincia || "",
            pais: data.direccion?.pais || ""
          });
        } else {
          Swal.fire("Error", "No se encontró el huésped", "error");
          router.back();
        }
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Fallo al conectar con el servidor", "error");
      } finally {
        setLoading(false);
      }
    };

    if (dniUrl) cargarHuesped();
  }, [dniUrl, router]);

  // --- 2. HELPERS DE VALIDACIÓN ---
  const calcularEdad = (fechaNacimiento: string): number => {
    if (!fechaNacimiento) return 0;
    const hoy = new Date();
    const nacimiento = new Date(fechaNacimiento);
    let edad = hoy.getFullYear() - nacimiento.getFullYear();
    const m = hoy.getMonth() - nacimiento.getMonth();
    if (m < 0 || (m === 0 && hoy.getDate() < nacimiento.getDate())) {
        edad--;
    }
    return edad;
  };

  const getInputClass = (fieldName: string) => {
      return `w-full border p-2 rounded focus:ring-2 focus:ring-blue-400 outline-none ${errores[fieldName] ? 'border-red-500 bg-red-50' : ''}`;
  };

  // --- 3. MANEJO DE INPUTS (REGEX) ---
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;

    // Validaciones al escribir (bloqueo de teclas)
    const camposSoloTexto = ["nombre", "apellido", "nacionalidad", "ocupacion", "pais", "provincia", "localidad"];
    const camposSoloNum = ["dni", "cuil", "codPostal", "numero", "piso"]; 

    // Solo letras
    if (camposSoloTexto.includes(name)) {
      if (!/^[a-zA-Z\u00C0-\u00FF\s]*$/.test(value)) return;
    }

    // Solo números
    if(camposSoloNum.includes(name)){
       if (!/^[0-9]*$/.test(value)) return;
    }

    // Teléfono especial
    if (name === "telefono") {
       if (!/^[0-9+\-\s]*$/.test(value)) return;
    }

    setForm({ ...form, [name]: value });

    // Limpiar error visual al escribir
    if (errores[name]) {
        setErrores(prev => {
            const newErrors = { ...prev };
            delete newErrors[name];
            return newErrors;
        });
    }
  };

  // --- 4. VALIDACIÓN GENERAL (POP-UP) ---
  const validarFormulario = (): boolean => {
    const nuevosErrores: Record<string, string> = {};
    const mensajesPopup: string[] = [];

    const registrarError = (campo: string, mensaje: string) => {
        nuevosErrores[campo] = mensaje;
        mensajesPopup.push(mensaje);
    };

    // Campos Obligatorios
    if (!form.apellido) registrarError("apellido", "El apellido es obligatorio.");
    if (!form.nombre) registrarError("nombre", "El nombre es obligatorio.");
    if (!form.dni) registrarError("dni", "El DNI es obligatorio.");
    // if (!form.cuil) registrarError("cuil", "El CUIL es obligatorio."); // Descomentar si es obligatorio en Modificación
    if (!form.fechaNacimiento) registrarError("fechaNacimiento", "La fecha de nacimiento es obligatoria.");
    if (!form.telefono) registrarError("telefono", "El teléfono es obligatorio.");
    if (!form.nacionalidad) registrarError("nacionalidad", "La nacionalidad es obligatoria.");
    if (!form.ocupacion) registrarError("ocupacion", "La ocupación es obligatoria.");

    // Dirección
    if (!form.calle) registrarError("calle", "La calle es obligatoria.");
    if (!form.numero) registrarError("numero", "La altura (número) es obligatoria."); 
    if (!form.localidad) registrarError("localidad", "La localidad es obligatoria.");
    if (!form.codPostal) registrarError("codPostal", "El código postal es obligatorio.");

    // Reglas Específicas
    if (form.dni && form.dni.length !== 8) {
        registrarError("dni", "El DNI debe tener exactamente 8 dígitos.");
    }
    if (form.cuil && form.cuil.length !== 11 && form.cuil.length > 0) { // Validar solo si escribió algo
        registrarError("cuil", "El CUIL debe tener exactamente 11 dígitos.");
    }
    if (form.telefono) {
        
        const soloNumerosTel = form.telefono.replace(/[^0-9]/g, "");
        
        //Mínimo 10 dígitos reales
        if (soloNumerosTel.length < 10) {
            registrarError("telefono", "El teléfono debe tener al menos 10 números.");
        }
        //Máximo 15 dígitos reales
        else if (soloNumerosTel.length > 15) {
            registrarError("telefono", "El teléfono no puede tener más de 15 números.");
        }
    }
    
    if (form.fechaNacimiento) {
        const edad = calcularEdad(form.fechaNacimiento);
        if (edad < 1) registrarError("fechaNacimiento", "La edad del huésped no es válida.");
    }

    setErrores(nuevosErrores);

    if (mensajesPopup.length > 0) {
        const listaHtml = `
            <ul style="text-align: left; font-size: 0.9em; list-style-type: none; padding: 0; margin: 0;">
                ${mensajesPopup.map(msg => `<li style="margin-bottom: 6px; padding-left: 10px; border-left: 3px solid #d33;">${msg}</li>`).join('')}
            </ul>
        `;

        Swal.fire({
            title: 'Datos Incompletos o Inválidos',
            icon: 'warning',
            html: listaHtml,
            confirmButtonText: 'Revisar',
            confirmButtonColor: '#3085d6'
        });
        return false;
    }
    return true;
  };

  // --- 5. GUARDAR CAMBIOS ---
  const handleGuardar = async () => {
    // Primero validamos
    if (!validarFormulario()) return;

    setGuardando(true);
    const edadCalculada = calcularEdad(form.fechaNacimiento);
    
    const payload = {
        nombre: form.nombre,
        apellido: form.apellido,
        tipoDni: form.tipoDni,
        dni: parseInt(form.dni),
        fechaNacimiento: form.fechaNacimiento,
        edad: edadCalculada, // Importante mandar la edad recalculada
        email: form.email,
        telefono: form.telefono,
        ocupacion: form.ocupacion,
        posIva: form.posIva,
        nacionalidad: form.nacionalidad,
        direccion: {
            calle: form.calle,
            numero: parseInt(form.numero) || 0,
            departamento: form.departamento,
            piso: form.piso,
            codPostal: parseInt(form.codPostal) || 0,
            localidad: form.localidad, 
            provincia: form.provincia,
            pais: form.pais            
        }
    };

    try {
        const res = await fetch(`http://localhost:8081/huespedes/${form.tipoDni}/${form.dni}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            await Swal.fire({
                title: "¡Éxito!", 
                text: "Huésped modificado correctamente", 
                icon: "success",
                timer: 2000
            });
            router.push("/dashboard/huesped");
        } else {
            const txt = await res.text();
            Swal.fire("Error", "No se pudo guardar: " + txt, "error");
        }
    } catch (e) {
        Swal.fire("Error", "Error de conexión", "error");
    } finally {
        setGuardando(false);
    }
  };

  if (loading) return <div className="p-8 text-center text-gray-600 font-bold">Cargando datos del huésped...</div>;

  return (
    <div className="p-8 max-w-5xl mx-auto bg-gray-50 min-h-screen font-sans text-gray-800">
      <h1 className="text-3xl font-bold text-center mb-8 text-gray-800">Modificar Huésped</h1>

      <div className="bg-white p-8 rounded-xl shadow-lg border border-gray-200">
        
        {/* --- DATOS PERSONALES --- */}
        <h3 className="text-xl font-bold text-gray-700 mb-4 border-b pb-2">Datos Personales</h3>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            {/* DNI y Tipo (Deshabilitados para edición, pero visibles) */}
            <div className="bg-gray-100 p-3 rounded border border-gray-300">
                <label className="text-xs font-bold text-gray-500 uppercase mb-1 block">Documento (No editable)</label>
                <div className="flex gap-2">
                    <span className="font-mono text-lg font-bold text-gray-700">{form.tipoDni}</span>
                    <span className="font-mono text-lg font-bold text-gray-800">{form.dni}</span>
                </div>
            </div>

            {/* Fecha Nacimiento */}
            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Fecha Nacimiento <span className="text-red-500">(*)</span></label>
                <input type="date" name="fechaNacimiento" value={form.fechaNacimiento} onChange={handleChange} className={getInputClass("fechaNacimiento")} />
            </div>

            {/* Nombre y Apellido */}
            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Nombre <span className="text-red-500">(*)</span></label>
                <input name="nombre" value={form.nombre} onChange={handleChange} className={getInputClass("nombre")} />
            </div>
            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Apellido <span className="text-red-500">(*)</span></label>
                <input name="apellido" value={form.apellido} onChange={handleChange} className={getInputClass("apellido")} />
            </div>

            {/* CUIL y Teléfono */}
            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">CUIL</label>
                <input name="cuil" maxLength={11} value={form.cuil} onChange={handleChange} className={getInputClass("cuil")} placeholder="11 dígitos" />
            </div>
            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Teléfono <span className="text-red-500">(*)</span></label>
                <input name="telefono" maxLength={20} value={form.telefono} onChange={handleChange} className={getInputClass("telefono")} />
            </div>

            {/* Email y Nacionalidad */}
            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Email</label>
                <input name="email" value={form.email} onChange={handleChange} className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none" />
            </div>
            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Nacionalidad <span className="text-red-500">(*)</span></label>
                <input name="nacionalidad" value={form.nacionalidad} onChange={handleChange} className={getInputClass("nacionalidad")} />
            </div>

            {/* Ocupación y Posición IVA */}
            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Ocupación <span className="text-red-500">(*)</span></label>
                <input name="ocupacion" value={form.ocupacion} onChange={handleChange} className={getInputClass("ocupacion")} />
            </div>
            <div>
                <label className="block text-sm font-bold text-gray-700 mb-1">Posición Frente al IVA</label>
                <select name="posIva" value={form.posIva} onChange={handleChange} className="w-full border p-2 rounded bg-white focus:ring-2 focus:ring-blue-500 outline-none">
                    <option>Consumidor Final</option>
                    <option>Responsable Inscripto</option>
                    <option>Monotributista</option>
                    <option>Exento</option>
                </select>
            </div>
        </div>

        {/* --- DIRECCIÓN (DISEÑO LIMPIO) --- */}
        <div className={`bg-white p-4 border rounded shadow-sm mt-8 ${errores.calle || errores.numero || errores.codPostal || errores.localidad ? 'border-red-300' : 'border-gray-300'}`}>
            <label className="block text-sm font-bold text-blue-800 mb-2 border-b pb-1">Dirección <span className="text-red-500">(*)</span></label>
            
            <div className="mb-2">
                <input name="calle" value={form.calle} onChange={handleChange} placeholder="Calle (*)" className={getInputClass("calle")} />
            </div>
            
            <div className="flex gap-2 mb-2">
                <div className="w-1/3">
                    <input name="numero" value={form.numero} onChange={handleChange} placeholder="Núm (*)" className={getInputClass("numero")} />
                </div>
                <div className="w-1/3">
                    <input name="piso" value={form.piso} onChange={handleChange} placeholder="Piso" className={getInputClass("piso")} />
                </div>
                <div className="w-1/3">
                    <input name="departamento" value={form.departamento} onChange={handleChange} placeholder="Depto" className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none" />
                </div>
            </div>

            <div className="flex gap-2 mb-4">
                <div className="w-1/3">
                    <input name="codPostal" value={form.codPostal} onChange={handleChange} placeholder="CP (*)" className={getInputClass("codPostal")} />
                </div>
                <div className="w-2/3">
                    <input name="localidad" value={form.localidad} onChange={handleChange} placeholder="Ciudad/Localidad (*)" className={getInputClass("localidad")} />
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
                <div>
                    <label className="text-xs font-bold text-gray-500 ml-1">Provincia</label>
                    <input name="provincia" value={form.provincia} onChange={handleChange} className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none" />
                </div>
                <div>
                    <label className="text-xs font-bold text-gray-500 ml-1">País</label>
                    <input name="pais" value={form.pais} onChange={handleChange} className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none" />
                </div>
            </div>
        </div>

        {/* --- BOTONES DE ACCIÓN --- */}
        <div className="flex justify-between pt-8 mt-6 border-t border-gray-200">
            <button 
                onClick={() => router.back()} 
                className="px-6 py-2.5 bg-gray-200 text-gray-700 font-bold rounded-lg hover:bg-gray-300 transition"
            >
                Cancelar
            </button>
            <button 
                onClick={handleGuardar} 
                disabled={guardando}
                className="px-8 py-2.5 bg-blue-600 text-white font-bold rounded-lg hover:bg-blue-700 shadow-md transition transform active:scale-95 disabled:opacity-50"
            >
                {guardando ? "Guardando..." : "Guardar Cambios"}
            </button>
        </div>

      </div>
    </div>
  );
}