import Head from "next/head";
import Layout from "../app/layout";
import AuthComponent from "./dashboard/usuario/ingresoUsuario";


export default function Home() {
  return (
    <div>
      <Head>
        <title>Gestion Hotel Sprints Masters</title>
        <meta name="descripcion" content="Gestion hotelera sprint masters" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <AuthComponent />
    </div>
  );
}
