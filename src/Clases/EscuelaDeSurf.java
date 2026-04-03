package Clases;

import Enumeradores.EstadoPago;
import Enumeradores.MetodoPago;
import Enumeradores.NombreEquipo;
import ExcepcionesPersonalizadas.CupoLlenoException;
import ExcepcionesPersonalizadas.IdNoEncontradoException;
import Interfaces.InterfazJson;
import Utiles.JsonUtiles;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class EscuelaDeSurf implements InterfazJson //Clase para encargarse de la gestión de datos y lógica de negocio
{
    private final Repositorio<Instructor> repoInstructores;
    private final Repositorio<ClaseDeSurf> repoClases;
    private final Repositorio<Alumno> repoAlumnos;
    private final Repositorio<Cliente> repoClientes;
    private final Repositorio<Reserva> repoReservas;
    private final Repositorio<Equipo> repoEquipos;
    private final Repositorio<Alquiler> repoAlquileres;
    private final Repositorio<Pago> repoPagos;

    /// CONSTRUCTOR

    public EscuelaDeSurf()
    {
        this.repoInstructores = new Repositorio<>();
        this.repoClases = new Repositorio<>();
        this.repoAlumnos = new Repositorio<>();
        this.repoClientes = new Repositorio<>();
        this.repoReservas = new Repositorio<>();
        this.repoEquipos = new Repositorio<>();
        this.repoAlquileres = new Repositorio<>();
        this.repoPagos = new Repositorio<>();
    }

    /// GETTERS

    public Repositorio<Instructor> getRepoInstructores()
    {
        return repoInstructores;
    }

    public Repositorio<ClaseDeSurf> getRepoClases()
    {
        return repoClases;
    }

    public Repositorio<Alumno> getRepoAlumnos()
    {
        return repoAlumnos;
    }

    public Repositorio<Cliente> getRepoClientes()
    {
        return repoClientes;
    }

    public Repositorio<Reserva> getRepoReservas()
    {
        return repoReservas;
    }

    public Repositorio<Equipo> getRepoEquipos()
    {
        return repoEquipos;
    }

    public Repositorio<Alquiler> getRepoAlquileres()
    {
        return repoAlquileres;
    }

    public Repositorio<Pago> getRepoPagos()
    {
        return repoPagos;
    }

    /// METODOS

    public void registrarNuevoAlumno(Alumno alumno)
    {
        if (alumno == null)
        {
            throw new IllegalArgumentException("El alumno no puede ser nulo");
        }
        getRepoAlumnos().agregar(alumno.getIdAlumno(), alumno);
    }

    public void registrarNuevoEquipo(Equipo equipo)
    {
        if (equipo == null)
        {
            throw new IllegalArgumentException("El equipo no puede ser nulo");
        }
        getRepoEquipos().agregar(equipo.getIdEquipo(), equipo);
    }

    public void registrarNuevoInstructor(Instructor instructor)
    {
        if (instructor == null)
        {
            throw new IllegalArgumentException("El instructor no puede ser nulo");
        }
        getRepoInstructores().agregar(instructor.getIdInstructor(), instructor);
    }

    public void registrarNuevaClase(ClaseDeSurf clase)
    {
        if (clase == null)
        {
            throw new IllegalArgumentException("La clase no puede ser nula");
        }
        getRepoClases().agregar(clase.getIdClase(), clase);
    }

    public void registrarNuevaReserva(Reserva reserva) throws CupoLlenoException, IdNoEncontradoException
    {
        if (reserva == null)
        {
            throw new IllegalArgumentException("La reserva no puede ser nula");
        }

        ClaseDeSurf clase = reserva.getClaseDeSurf();
        List<Alumno> alumnosInscritos = mostrarAlumnosDeUnaClase(clase.getIdClase());//este metodo lanza IdNoEncontradoException

        if (alumnosInscritos.size() >= clase.getCupoMax())
        {
            throw new CupoLlenoException("La clase ID " + clase.getIdClase() + " ya está llena.");
        }

        getRepoReservas().agregar(reserva.getIdReserva(), reserva);
        getRepoPagos().agregar(reserva.getPago().getIdPago(), reserva.getPago());
    }

    public void registrarNuevoCliente(Cliente cliente)
    {
        if (cliente == null)
        {
            throw new IllegalArgumentException("El alumno no puede ser nulo");
        }
        getRepoClientes().agregar(cliente.getIdCliente(), cliente);
    }

    public void registrarNuevoAlquiler(Alquiler alquiler)
    {
        if (alquiler == null)
        {
            throw new IllegalArgumentException("El alquiler no puede ser nulo");
        }
        getRepoAlquileres().agregar(alquiler.getIdAlquiler(), alquiler);
        getRepoPagos().agregar(alquiler.getPago().getIdPago(), alquiler.getPago());
    }

    public Alumno buscarAlumnoPorId(int id) throws IdNoEncontradoException
    {
        Alumno a = getRepoAlumnos().buscarPorId(id);

        if (a == null)
        {
            throw new IdNoEncontradoException("No se ha encontrado ningun alumno con el id ingresado.");
        }

        return a;
    }

    public Instructor buscarInstructorPorId(int id) throws IdNoEncontradoException
    {
        Instructor i = getRepoInstructores().buscarPorId(id);

        if (i == null)
        {
            throw new IdNoEncontradoException("No se ha encontrado ningun alumno con el id ingresado.");
        }

        return i;
    }

    public ClaseDeSurf buscarClasePorId(int id) throws IdNoEncontradoException
    {
        ClaseDeSurf c = getRepoClases().buscarPorId(id);

        if (c == null)
        {
            throw new IdNoEncontradoException("No se ha encontrado ninguna clase con el id ingresado.");
        }

        return c;
    }

    public Cliente buscarClientePorId(int id) throws IdNoEncontradoException
    {
        Cliente c = getRepoClientes().buscarPorId(id);

        if (c == null)
        {
            throw new IdNoEncontradoException("No se ha encontrado ningun cliente con el id ingresado: " + id);
        }

        return c;
    }

    public Equipo buscarEquipoPorId(int id) throws IdNoEncontradoException
    {
        Equipo e = getRepoEquipos().buscarPorId(id);

        if (e == null)
        {
            throw new IdNoEncontradoException("No se ha encontrado ningun equipo con el id ingresado: " + id);
        }

        return e;
    }

    public List<Equipo> buscarEquiposDisponiblesPorNombreOrdenadosPorPrecio(NombreEquipo nombreEquipo, int cantidad)
    {
        if (nombreEquipo == null)
        {
            throw new IllegalArgumentException("El nombre del equipo no puede ser nulo.");
        }
        if (cantidad <= 0)
        {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        List<Equipo> equiposDisponibles = getRepoEquipos().getTodos().stream()
                .filter(Equipo::isDisponible)
                .filter(equipo -> equipo.getNombre() == nombreEquipo)
                .sorted(Comparator.comparingDouble(Equipo::getPrecioPorDia))
                .limit(cantidad)
                .toList();

        if (equiposDisponibles.size() < cantidad)
        {
            return List.of();
        }

        return equiposDisponibles;
    }

    public List<Reserva> buscarReservasPorAlumnoId(int idAlumno) throws IdNoEncontradoException
    {
        Alumno alumno = buscarAlumnoPorId(idAlumno);

        List<Reserva> reservasDelAlumno = new ArrayList<>();

        for (Reserva reserva : getRepoReservas().getTodos())
        {
            if (reserva.getAlumno().getIdAlumno() == alumno.getIdAlumno())
            {
                reservasDelAlumno.add(reserva);
            }
        }

        return reservasDelAlumno;
    }

    private void pagar(Pago pago, MetodoPago metodo)
    {
        pago.setMetodoPago(metodo);
        pago.setFechaPago(LocalDate.now());
        pago.setEstadoPago(EstadoPago.REALIZADO);
    }

    public void pagarReserva(int idReserva, MetodoPago metodo) throws IdNoEncontradoException, IllegalStateException
    {
        Reserva reserva = getRepoReservas().buscarPorId(idReserva);
        if (reserva == null)
        {
            throw new IdNoEncontradoException("No se encontró ninguna reserva con el ID: " + idReserva);
        }

        Pago pago = reserva.getPago();
        if (pago.getEstadoPago() == EstadoPago.REALIZADO)
        {
            throw new IllegalStateException("Esta reserva ya se encuentra pagada.");
        }

        pagar(pago, metodo);
    }

    public void pagarAlquiler(int idAlquiler, MetodoPago metodo) throws IdNoEncontradoException, IllegalStateException
    {
        Alquiler alquiler = getRepoAlquileres().buscarPorId(idAlquiler);
        if (alquiler == null)
        {
            throw new IdNoEncontradoException("No se encontró ningún alquiler con el ID: " + idAlquiler);
        }

        Pago pago = alquiler.getPago();
        if (pago.getEstadoPago() == EstadoPago.REALIZADO)
        {
            throw new IllegalStateException("Este alquiler ya se encuentra pagado.");
        }

        pagar(pago, metodo);
    }

    public boolean chequearMorosidadAlumno(int idAlumno) throws IdNoEncontradoException
    {
        List<Reserva> reservas = buscarReservasPorAlumnoId(idAlumno);

        for (Reserva r : reservas)
        {
            if (r.getPago().esMoroso())
            {
                return true;
            }
        }

        return false; // Si termina el bucle, significa que no encontró pagos morosos
    }

    public boolean chequearMorosidadCliente(int idCliente) throws IdNoEncontradoException
    {
        Cliente cliente = getRepoClientes().buscarPorId(idCliente);
        if (cliente == null)
        {
            throw new IdNoEncontradoException("No se encontró ningún cliente con el ID: " + idCliente);
        }

        for (Alquiler a : cliente.getAlquileres())
        {
            if (a.getPago().esMoroso())
            {
                return true;
            }
        }

        return false;
    }

    public List<Alumno> mostrarAlumnosDeUnaClase(int idClase) throws IdNoEncontradoException
    {
        ClaseDeSurf clase = getRepoClases().buscarPorId(idClase);
        if (clase == null)
        {
            throw new IdNoEncontradoException("No fue encontrada ninguna clase con ese ID: " + idClase);
        }

        List<Alumno> arrAlumnos = new ArrayList<>();

        for (Reserva r : getRepoReservas().getTodos())
        {
            if (r.getClaseDeSurf().getIdClase() == idClase && r.isEstaActiva())
            {
                Alumno alumno = r.getAlumno();
                arrAlumnos.add(alumno);
            }
        }

        return arrAlumnos;
    }


    public void leerJsonDeRepositorios()
    {
        JsonUtiles.leerRepositorioDesdeJson(repoAlumnos, repoInstructores, repoClases, repoClientes, repoReservas, repoEquipos, repoAlquileres, repoPagos, "escuelaDeSurf.json");
    }

    @Override
    public JSONObject toJSON()
    {
        JSONObject escuelaJSON = new JSONObject();
        escuelaJSON.put("repoAlumnos", repoAlumnos.toJSON());
        escuelaJSON.put("repoInstructores", repoInstructores.toJSON());
        escuelaJSON.put("repoClases", repoClases.toJSON());
        escuelaJSON.put("repoClientes", repoClientes.toJSON());
        escuelaJSON.put("repoReservas", repoReservas.toJSON());
        escuelaJSON.put("repoEquipos", repoEquipos.toJSON());
        escuelaJSON.put("repoAlquileres", repoAlquileres.toJSON());
        escuelaJSON.put("repoPagos", repoPagos.toJSON());
        return escuelaJSON;
    }
}
