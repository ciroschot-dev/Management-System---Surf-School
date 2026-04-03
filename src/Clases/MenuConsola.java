package Clases;

import Enumeradores.MetodoPago;
import Enumeradores.NivelDeSurf;
import Enumeradores.NombreEquipo;
import Enumeradores.TipoClase;
import ExcepcionesPersonalizadas.CupoLlenoException;
import ExcepcionesPersonalizadas.FechaInvalidaException;
import ExcepcionesPersonalizadas.IdNoEncontradoException;
import Utiles.JsonUtiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuConsola //Clase para encargarse de la gestión de la interfaz de usuario
{
    private final EscuelaDeSurf escuela; //La lógica de negocio
    private final Scanner scanner;

    public MenuConsola(EscuelaDeSurf escuela)
    {
        this.escuela = escuela;
        this.scanner = new Scanner(System.in);
    }

    public void ejecutarMenu()
    {
        int opcion = 0;

        do
        {
            System.out.println("\nMENU ESCUELA DE SURF\n");
            System.out.println("1. Agregar Alumno.");
            System.out.println("2. Agregar Instructor.");
            System.out.println("3. Agregar Clase de Surf.");
            System.out.println("4. Agregar Reserva.");
            System.out.println("5. Agregar Cliente.");
            System.out.println("6. Agregar Equipo.");
            System.out.println("7. Agregar Alquiler.");
            System.out.println("8. Cancelar Alquiler.");
            System.out.println("9. Cancelar Reserva.");
            System.out.println("10. Buscar alumno por su id.");
            System.out.println("11. Mostrar reservas de un alumno.");
            System.out.println("12. Mostrar alumnos inscriptos en una clase.");
            System.out.println("13. Mostrar los alquileres");
            System.out.println("14. Mostrar las reservas");
            System.out.println("15. Pagar una reserva de clase.");
            System.out.println("16. Pagar un alquiler de equipo.");
            System.out.println("17. Chequear morosidad de alumno.");
            System.out.println("18. Chequear morosidad de cliente.");
            System.out.println("19. Grabar repositorios a json.");
            System.out.println("20. Mostrar todos los repositorios.");
            System.out.println("21. Buscar Home Deco x50 más barato (2 unidades).");

            System.out.println("999. Salir.");

            try
            {
                System.out.print("Ingrese una de las opciones: ");
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion)
                {
                    case 1 -> agregarAlumno();
                    case 2 -> agregarInstructor();
                    case 3 -> agregarClaseDeSurf();
                    case 4 -> agregarReserva();
                    case 5 -> agregarCliente();
                    case 6 -> agregarEquipo();
                    case 7 -> agregarAlquiler();
                    case 8 -> cancelarAlquiler();
                    case 9 -> cancelarReserva();
                    case 10 -> buscarAlumnoPorId();
                    case 11 -> mostrarReservasAlumno();
                    case 12 -> mostrarAlumnosInscriptosEnClase();
                    case 13 -> mostrarAlquileres();
                    case 14 -> mostrarReservas();
                    case 15 -> pagarUnaReserva();
                    case 16 -> pagarUnAlquiler();
                    case 17 -> chequearMorosidadAlumno();
                    case 18 -> chequearMorosidadCliente();
                    case 19 -> grabarRepositoriosAjson();
                    case 20 -> mostrarTodosLosRepositorios();
                    case 21 -> buscarHomeDecoX50MasBaratoDosUnidades();
                    case 999 -> System.out.println("\nSaliendo del programa...");
                    default -> System.out.println("\nIngrese una opción valida...");
                }
            }
            catch (InputMismatchException e)
            {
                System.out.println("\nError: debes ingresar un número.");
                scanner.nextLine();
            }
            catch (Exception e)
            {
                System.out.println("\nError inesperado: " + e.getMessage());
            }
        } while (opcion != 999);
    }

    private boolean deseaContinuar(String mensaje)
    {
        while (true) // Bucle infinito hasta que se ingrese 's' o 'n'
        {
            try
            {
                System.out.print(mensaje + " (s/n): ");
                char entrada = scanner.next().toLowerCase().charAt(0);
                scanner.nextLine();

                if (entrada == 's')
                {
                    return true;
                }
                if (entrada == 'n')
                {
                    return false;
                }

                throw new IllegalArgumentException("Error: debes ingresar 's' o 'n'.");
            }
            catch (Exception e)
            {
                System.out.println("❌ Error inesperado: " + e.getMessage());
            }
        }
    }

    public void mostrarTodosLosRepositorios()
    {
        System.out.println("--------------------------------------------------\n");
        System.out.println("REPOSITORIO DE ALUMNOS: " + escuela.getRepoAlumnos());
        System.out.println("\n--------------------------------------------------\n");
        System.out.println("REPOSITORIO DE INSTRUCTORES: " + escuela.getRepoInstructores());
        System.out.println("\n--------------------------------------------------\n");
        System.out.println("REPOSITORIO DE CLIENTES: " + escuela.getRepoClientes());
        System.out.println("\n--------------------------------------------------\n");
        System.out.println("REPOSITORIO DE CLASES: " + escuela.getRepoClases());
        System.out.println("\n--------------------------------------------------\n");
        System.out.println("REPOSITORIO DE RESERVAS: " + escuela.getRepoReservas());
        System.out.println("\n--------------------------------------------------\n");
        System.out.println("REPOSITORIO DE EQUIPOS: " + escuela.getRepoEquipos());
        System.out.println("\n--------------------------------------------------\n");
        System.out.println("REPOSITORIO DE ALQUILERES: " + escuela.getRepoAlquileres());
        System.out.println("\n--------------------------------------------------\n");
        System.out.println("REPOSITORIO DE PAGOS: " + escuela.getRepoPagos());
        System.out.println("\n--------------------------------------------------");
    }

    public void buscarHomeDecoX50MasBaratoDosUnidades()
    {
        try
        {
            List<Equipo> equipos = escuela.buscarEquiposDisponiblesPorNombreOrdenadosPorPrecio(NombreEquipo.HOME_DECO_X50, 2);
            if (equipos.isEmpty())
            {
                System.out.println("No hay al menos 2 unidades disponibles de Home Deco x50.");
                return;
            }

            double total = 0;
            System.out.println("Las 2 unidades de Home Deco x50 más baratas disponibles son:");
            for (Equipo equipo : equipos)
            {
                System.out.println(equipo);
                total += equipo.getPrecioPorDia();
            }
            System.out.println("Total por día (2 unidades): $" + total);
        }
        catch (Exception e)
        {
            System.out.println("❌ Error al buscar Home Deco x50: " + e.getMessage());
        }
    }

    public void agregarAlumno()
    {
        do
        {
            Alumno alumno = crearNuevoAlumno();

            if (alumno != null)
            {
                escuela.registrarNuevoAlumno(alumno);
                System.out.println("Alumno agregado correctamente.");
            }
            else
            {
                System.out.println("Creación de alumno cancelada o fallida.");
            }

        } while (deseaContinuar("Desea seguir cargando alumnos?"));
    }

    private Alumno crearNuevoAlumno() //para crear un solo alumno
    {
        try
        {
            System.out.println("CARGA DE DATOS DE ALUMNO\n");

            System.out.print("Ingrese el numero de DNI: ");
            String dni = scanner.nextLine().trim();

            System.out.print("Ingrese el nombre del alumno: ");
            String nombre = scanner.nextLine().trim();

            System.out.print("Ingrese el apellido del alumno: ");
            String apellido = scanner.nextLine().trim();

            System.out.print("Ingrese la edad del alumno: ");
            int edad = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Ingrese el numero de telefono: ");
            String telefono = scanner.nextLine().trim();

            System.out.print("Ingrese el nivel de surf(Principiante/Intermedio/Avanzado): ");
            String nivelTexto = scanner.nextLine().trim().toUpperCase();
            NivelDeSurf nivel = NivelDeSurf.valueOf(nivelTexto);

            System.out.print("Ingrese la cant de clases tomadas por el alumno: ");
            int cantDeClases = scanner.nextInt();
            scanner.nextLine();

            return new Alumno(dni, nombre, apellido, edad, telefono, nivel, cantDeClases);
        }
        catch (InputMismatchException e)
        {
            System.out.println("❌ Error: debes ingresar un tipo de dato valido.");
            scanner.nextLine();
            return null;
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("❌ Error de datos al crear el alumno: " + e.getMessage());
            return null;
        }
        catch (Exception e)
        {
            System.out.println("⚠️ Error inesperado al procesar el alumno: " + e.getMessage());
            return null;
        }
    }

    public void agregarEquipo()
    {
        do
        {
            Equipo equipo = crearNuevoEquipo();

            if (equipo != null)
            {
                escuela.registrarNuevoEquipo(equipo);
                System.out.println("Equipo agregado correctamente.");
            }
            else
            {
                System.out.println("Creación de equipo cancelada o fallida.");
            }

        } while (deseaContinuar("Desea seguir cargando equipos?"));
    }

    private Equipo crearNuevoEquipo()
    {
        try
        {
            System.out.println("CARGA DE DATOS DE EQUIPOS\n");

            NombreEquipo[] tiposDeEquipo = NombreEquipo.values();

            System.out.println("Seleccione el tipo de equipo:");
            for (int i = 0; i < tiposDeEquipo.length; i++)
            {
                System.out.println((i) + ". " + tiposDeEquipo[i].getNombreEquipo());
            }
            System.out.print("Ingrese el número de la opción: ");

            int seleccion = scanner.nextInt();
            scanner.nextLine();

            if (seleccion < 0 || seleccion >= tiposDeEquipo.length)
            {
                throw new IllegalArgumentException("Selección inválida. Ingrese un número entre 0 y " + tiposDeEquipo.length + ".");
            }

            NombreEquipo nombreEnum = tiposDeEquipo[seleccion];

            return new Equipo(nombreEnum);
        }
        catch (InputMismatchException e)
        {
            System.out.println("❌ Error: debes ingresar un tipo de dato valido.");
            scanner.nextLine();
            return null;
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("❌ Error de datos: " + e.getMessage());
            return null;
        }
        catch (Exception e)
        {
            System.out.println("⚠️ Error inesperado: " + e.getMessage());
            return null;
        }
    }

    public void agregarInstructor()
    {
        do
        {
            Instructor instructor = crearNuevoInstructor();

            if (instructor != null)
            {
                escuela.registrarNuevoInstructor(instructor);
                System.out.println("Instructor agregado correctamente.");
            }
            else
            {
                System.out.println("Creación de instructor cancelada o fallida.");
            }

        } while (deseaContinuar("Desea seguir cargando instructores?"));
    }

    public Instructor crearNuevoInstructor()
    {
        try
        {
            System.out.println("CARGA DE DATOS DE INSTRUCTOR\n");

            System.out.print("Ingrese el numero de DNI: ");
            String dni = scanner.nextLine().trim();

            System.out.print("Ingrese el nombre del instructor: ");
            String nombre = scanner.nextLine().trim();

            System.out.print("Ingrese el apellido del instructor: ");
            String apellido = scanner.nextLine().trim();

            System.out.print("Ingrese la edad del instructor: ");
            int edad = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Ingrese el numero de telefono: ");
            String telefono = scanner.nextLine().trim();

            System.out.print("Ingrese los años de experiencia del instructor: ");
            int aniosExp = scanner.nextInt();
            scanner.nextLine();

            return new Instructor(dni, nombre, apellido, edad, telefono, aniosExp);
        }
        catch (InputMismatchException e)
        {
            System.out.println("❌ Error: debes ingresar un tipo de dato valido.");
            scanner.nextLine();
            return null;
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("❌ Error de datos: " + e.getMessage());
            return null;
        }
        catch (Exception e)
        {
            System.out.println("⚠️ Error inesperado: " + e.getMessage());
            return null;
        }
    }

    public void agregarClaseDeSurf()
    {
        do
        {
            ClaseDeSurf clase = crearNuevaClaseDeSurf();

            if (clase != null)
            {
                escuela.registrarNuevaClase(clase);
                System.out.println("Clase agregada correctamente.");
            }
            else
            {
                System.out.println("Creación de clase cancelada o fallida.");
            }

        } while (deseaContinuar("Desea seguir cargando clases de surf?"));
    }

    private ClaseDeSurf crearNuevaClaseDeSurf()
    {
        try
        {
            System.out.println("CARGA DE DATOS DE UNA CLASE DE SURF\n");

            //INSTRUCTOR
            int idInstructor = -1;
            boolean flag = true;
            do
            {
                try
                {
                    System.out.print("Ingrese el id del instructor que dictará la clase: ");
                    idInstructor = scanner.nextInt();
                    scanner.nextLine();
                    flag = true;
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Error: debes ingresar un numero.");
                    scanner.nextLine();
                    flag = false;
                }
            } while (!flag);//mientras que sea false sigue.

            Instructor instructor = escuela.buscarInstructorPorId(idInstructor);

            //TIPO DE CLASE
            System.out.println("Ingrese el tipo de clase: ");
            System.out.println("1. Clase grupal");
            System.out.println("2. Clase particular");
            System.out.print("Opción: ");
            int respuesta = scanner.nextInt();
            scanner.nextLine();

            TipoClase tipoClase = null;
            switch (respuesta)
            {
                case 1 -> tipoClase = TipoClase.GRUPAL;
                case 2 -> tipoClase = TipoClase.PARTICULAR;
                default -> throw new IllegalArgumentException("Opción de tipo de clase no válida.");
            }

            //FECHA Y HORA
            System.out.print("Ingrese la fecha de la clase (formato YYYY-MM-DD): ");
            String fechaStr = scanner.nextLine().trim();
            System.out.print("Ingrese la hora de la clase (formato HH:MM, 24hs): ");
            String horaStr = scanner.nextLine().trim();

            String fechaHoraStr = fechaStr + " " + horaStr;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr, formatter);

            //CREACION DE CLASE

            return new ClaseDeSurf(instructor, tipoClase, fechaHora);

        }
        catch (InputMismatchException e)
        {
            System.out.println("❌ Error: debes ingresar un tipo de dato valido.");
            scanner.nextLine();
            return null;
        }
        catch (DateTimeParseException e)
        {
            System.out.println("❌ Error en el formato de fecha u hora. Use YYYY-MM-DD y HH:MM.");
            return null;
        }
        catch (IdNoEncontradoException e)
        {
            System.out.println("❌ Error: " + e.getMessage());
            return null;
        }
        catch (FechaInvalidaException e)
        {
            System.out.println("❌ Error al crear la clase: " + e.getMessage());
            return null;
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("❌ Error de datos: " + e.getMessage());
            return null;
        }
        catch (Exception e)
        {
            System.out.println("⚠️ Error inesperado: " + e.getMessage());
            return null;
        }
    }

    public void agregarReserva()
    {
        do
        {
            Reserva reserva = crearNuevaReserva();

            if (reserva != null)
            {
                try
                {
                    escuela.registrarNuevaReserva(reserva);
                    System.out.println("Reserva agregada correctamente.");
                }
                catch (CupoLlenoException | IdNoEncontradoException e)
                {
                    System.out.println("❌ Error al registrar: " + e.getMessage());
                }
            }
            else
            {
                System.out.println("Creación de reserva cancelada o fallida.");
            }

        } while (deseaContinuar("Desea seguir cargando reservas?"));
    }

    public Reserva crearNuevaReserva()
    {
        try
        {
            System.out.println("CARGA DE DATOS A UNA RESERVA\n");

            //Elijo si es una reserva para un alumno nuevo o ya existe
            System.out.println("1) Seleccionar un alumno ya existente");
            System.out.println("2) Registrar un nuevo alumno");
            System.out.print("Ingrese la opción: ");
            int opcionAlumno = scanner.nextInt();
            scanner.nextLine();

            Alumno alumno = null;

            //listar alumno ya existente para que elija
            if (opcionAlumno == 1)
            {
                if (escuela.getRepoAlumnos().getDatos().isEmpty())
                {
                    System.out.println("No hay alumnos previamente cargados.");
                    opcionAlumno = 2; //fuerzo la creación de uno nuevo
                }
                else
                {
                    System.out.println("Listado de alumnos existente: ");
                    escuela.getRepoAlumnos().getTodos().forEach(System.out::println);

                    System.out.print("Ingrese el ID del alumno: ");
                    int idAlumno = scanner.nextInt();
                    scanner.nextLine();

                    try
                    {
                        alumno = escuela.buscarAlumnoPorId(idAlumno);
                    }
                    catch (IdNoEncontradoException e)
                    {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }
            if (opcionAlumno == 2)
            {
                alumno = crearNuevoAlumno();

                if (alumno == null)
                {
                    System.out.println("❌ Error al crear el alumno. Abortando la creación de la reserva.");
                    return null;
                }

                escuela.registrarNuevoAlumno(alumno);
                System.out.println("✅ Alumno nuevo registrado.");
            }

            //Elijo si selec. una clase existente o una nueva

            System.out.println("1) Seleccionar una clase ya existente");
            System.out.println("2) Registrar una nueva clase");
            System.out.print("Ingrese la opción: ");
            int opcionClase = scanner.nextInt();
            scanner.nextLine();

            ClaseDeSurf clase = null;

            if (opcionClase == 1)
            {
                if (escuela.getRepoClases().getDatos().isEmpty())
                {
                    System.out.println("No hay clases previamente cargadas.");
                    opcionClase = 2; //fuerzo la creación de uno nuevo
                }
                else
                {
                    System.out.println("Listado de clases existentes: ");
                    escuela.getRepoClases().getTodos().forEach(System.out::println);

                    System.out.print("Ingrese el ID de la clase: ");
                    int idClase = scanner.nextInt();
                    scanner.nextLine();

                    try
                    {
                        clase = escuela.buscarClasePorId(idClase);
                    }
                    catch (IdNoEncontradoException e)
                    {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }

            if (opcionClase == 2)
            {
                clase = crearNuevaClaseDeSurf();

                if (clase == null)
                {
                    System.out.println("❌ Error al crear la clase. Abortando creación de reserva.");
                    return null;
                }

                escuela.registrarNuevaClase(clase);
                System.out.println("✅ Clase nueva registrada.");
            }

            //verifico el cupo de la clase
            if (!clase.hayCuposDisponible())
            {
                throw new CupoLlenoException();
            }

            clase.setCuposOcupados(clase.getCuposOcupados() + 1);

            try
            {
                return new Reserva(alumno, clase);
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("❌ Error al crear la reserva: " + e.getMessage());
                return null;
            }
            catch (Exception e)
            {
                System.out.println("❌ Error inesperado al registrar la reserva: " + e.getMessage());
                return null;
            }
        }
        catch (Exception e)
        {
            System.out.println("⚠️ Error inesperado: " + e.getMessage());
            return null;
        }
    }

    public void agregarCliente()
    {
        do
        {
            Cliente cliente = crearNuevoCliente();

            if (cliente != null)
            {
                escuela.registrarNuevoCliente(cliente);
                System.out.println("Cliente agregado correctamente.");
            }
            else
            {
                System.out.println("Creación de cliente cancelada o fallida.");
            }

        } while (deseaContinuar("Desea seguir cargando clientes?"));
    }

    public Cliente crearNuevoCliente()
    {
        try
        {
            System.out.println("CARGA DE DATOS DE CLIENTE\n");

            System.out.print("Ingrese el numero de DNI: ");
            String dni = scanner.nextLine().trim();

            System.out.print("Ingrese el nombre del cliente: ");
            String nombre = scanner.nextLine().trim();

            System.out.print("Ingrese el apellido del cliente: ");
            String apellido = scanner.nextLine().trim();

            System.out.print("Ingrese la edad del ciente: ");
            int edad = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Ingrese el numero de telefono: ");
            String telefono = scanner.nextLine().trim();

            return new Cliente(dni, nombre, apellido, edad, telefono);
        }
        catch (InputMismatchException e)
        {
            System.out.println("❌ Error: debes ingresar un tipo de dato valido.");
            scanner.nextLine();
            return null;
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("❌ Error de datos al crear el cliente: " + e.getMessage());
            return null;
        }
        catch (Exception e)
        {
            System.out.println("⚠️ Error inesperado al procesar el cliente: " + e.getMessage());
            return null;
        }
    }

    public void agregarAlquiler()
    {
        do
        {
            Alquiler alquiler = crearNuevoAlquiler();

            if (alquiler != null)
            {
                try
                {
                    escuela.registrarNuevoAlquiler(alquiler);

                    alquiler.getCliente().agregarAlquiler(alquiler);

                    System.out.println("✅ Alquiler registrado y vinculado al cliente correctamente.");
                }
                catch (Exception e)
                {
                    System.out.println("❌ Error al guardar el alquiler: " + e.getMessage());
                }
            }
            else
            {
                System.out.println("⚠️ La carga del alquiler fue cancelada.");
            }

        } while (deseaContinuar("Desea seguir cargando alquilers?"));
    }

    public Alquiler crearNuevoAlquiler()
    {
        try
        {
            System.out.println("CARGA DE DATOS DE ALQUILER\n");

            System.out.println("Ingrese la fecha de fin del alquier (formato: YYYY-MM-DD): ");
            String fecha = scanner.nextLine();
            //uso un catch para indicar si la fecha tiene formato incorrecto
            LocalDate fechaFin = LocalDate.parse(fecha);

            //ASIGNAMOS UN CLIENTE
            System.out.println("\nListado de Clientes:");
            //VALIDO QUE YA EXISTA UN CLIENTE
            if (escuela.getRepoClientes().getTodos().isEmpty())
            {
                System.out.println("⚠ No hay cliente registrados.");

                if (deseaContinuar("¿Desea registrar un nuevo cliente?"))
                {
                    agregarCliente();
                }
                else
                {
                    System.out.println("❌ No es posible realizar un alquiler sin un cliente.");
                }
            }

            escuela.getRepoClientes().getTodos().forEach(System.out::println);

            System.out.print("\nIngrese el ID del cliente que realiza el alquiler: ");
            int idCliente = scanner.nextInt();
            scanner.nextLine();
            Cliente cliente = escuela.buscarClientePorId(idCliente);

            //CREO EL ALQUILER
            Alquiler alquiler = new Alquiler(fechaFin, cliente);

            //VALIDACION DE EQUIPOS DISPONIBLES
            if (escuela.getRepoEquipos().getTodos().stream().noneMatch(Equipo::isDisponible))
            {
                System.out.println("⚠ No hay equipos disponibles.");

                if (deseaContinuar("¿Desea registrar nuevos equipos?"))
                {
                    agregarEquipo();
                }
                else
                {
                    System.out.println("❌ No es posible realizar un alquiler sin equipos.");
                }
            }
            boolean seguirAgregandoEquipos = true;
            do
            {
                System.out.println("\nEquipos Disponibles:");
                // Muestra solo equipos disponibles
                escuela.getRepoEquipos().getTodos().stream()
                        .filter(Equipo::isDisponible) // Filtra solo los disponibles
                        .forEach(System.out::println);

                System.out.print("\nIngrese el ID del equipo a agregar (o 0 para terminar): ");
                int idEquipo = scanner.nextInt();
                scanner.nextLine();

                if (idEquipo == 0)
                {
                    seguirAgregandoEquipos = false;
                }
                else
                {
                    try
                    {
                        Equipo equipo = escuela.buscarEquipoPorId(idEquipo);
                        if (equipo.isDisponible())
                        {
                            alquiler.agregarEquipo(equipo);
                            System.out.println("✅ Equipo agregado: " + equipo.getNombre());
                        }
                        else
                        {
                            System.out.println("Error: el equipo seleccionado no esta disponible.");
                        }
                    }
                    catch (IdNoEncontradoException e)
                    {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            } while (seguirAgregandoEquipos);

            if (alquiler.getEquiposAlquilados().isEmpty())
            {
                System.out.println("No se seleccionó ningún equipo. Se cancela el alquiler.");
                return null;
            }

            return alquiler;
        }
        catch (DateTimeParseException e)
        {
            System.out.println("❌ Error en el formato de fecha. Use YYYY-MM-DD");
            return null;
        }
        catch (InputMismatchException e)
        {
            System.out.println("❌ Error: debes ingresar un tipo de dato valido.");
            scanner.nextLine();
            return null;
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("❌ Error de datos: " + e.getMessage());
            return null;
        }
        catch (Exception e)
        {
            System.out.println("⚠️ Error inesperado: " + e.getMessage());
            return null;
        }
    }

    public void buscarAlumnoPorId()
    {
        //Pedimos que el usuario ingrese el id
        int id = 0;
        try
        {
            System.out.print("Ingresa el id de el alumno a buscar: ");
            id = scanner.nextInt();
            scanner.nextLine();
        }
        catch (InputMismatchException e)
        {
            System.out.println("\nError: ingresa un valor correcto. " + e.getMessage());
            scanner.nextLine();
        }
        catch (Exception e)
        {
            System.out.println("\nError inesperado: " + e.getMessage());
        }

        //Buscamos el alumno con ese id
        try
        {
            Alumno alumnito = escuela.buscarAlumnoPorId(id);
            System.out.println("✅ Alumno encontrado!");
            System.out.println(alumnito);
        }
        catch (IdNoEncontradoException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void mostrarReservasAlumno()
    {
        //le pedimos al usuario el id del alumno para ver sus reservas
        int id = 0;
        try
        {
            System.out.print("Ingresa el id del alumno para ver sus reservas: ");
            id = scanner.nextInt();
            scanner.nextLine();
        }
        catch (InputMismatchException e)
        {
            System.out.println("❌ Error: debes ingresar un número.");
            scanner.nextLine();
            return;
        }

        //mostramos las reservas del alumno
        try
        {
            List<Reserva> reservas = escuela.buscarReservasPorAlumnoId(id);//este metodo lanza la excepcion de idNoEncontrado

            if (reservas.isEmpty())
            {
                System.out.println("El alumno con ID " + id + " existe, pero no tiene reservas activas.");
            }
            else
            {
                System.out.println("Mostrando " + reservas.size() + " reserva(s) del alumno ID " + id + ":");
                for (Reserva r : reservas)
                {
                    r.mostrarReservaMejorada();
                }
            }
        }
        catch (IdNoEncontradoException e)
        {
            System.out.println("❌ Error: " + e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("⚠️ Error inesperado: " + e.getMessage());
        }
    }

    public void mostrarAlquileres()
    {
        if (escuela.getRepoAlquileres().getTodos().isEmpty())
        {
            System.out.println("⚠️ No se encuentrar alquileres registrados.");
            return;
        }

        System.out.println("════════════ MOSTRAR ALQUILERES ════════════");
        System.out.println("1) MOSTRAR SOLO ACTIVOS");
        System.out.println("2) MOSTRAR SOLO FINALIZADOS");
        System.out.println("3) MOSTRAR TODOS");
        System.out.println("Seleccione una opción: ");

        int opcion;

        try
        {
            opcion = Integer.parseInt(scanner.nextLine());
        }
        catch (NumberFormatException e)
        {
            System.out.println("⚠️ Debes ingresar un número válido.");
            return;
        }

        System.out.println("════════════ RESULTADO ════════════");
        switch (opcion)
        {
            case 1:
                System.out.println("ALQUILERES ACTIVOS: ");
                escuela.getRepoAlquileres().getTodos().stream()
                        .filter(Alquiler::isEstaActivo)
                        .forEach(Alquiler::mostrarAlquiler);
                break;

            case 2:
                System.out.println("ALQUILERES FINALIZADOS: ");
                escuela.getRepoAlquileres().getTodos().stream()
                        .filter(a -> !a.isEstaActivo())
                        .forEach(Alquiler::mostrarAlquiler);
                break;

            case 3:
                System.out.println("TODOS LOS ALQUILERES: ");
                escuela.getRepoAlquileres().getTodos().forEach(Alquiler::mostrarAlquiler);
                break;

            default:
                System.out.println("Opción no válida.");

        }
        System.out.println("═══════════════════════════════════════════════\n");
    }

    public void cancelarAlquiler()
    {
        try
        {

            System.out.print("Ingrese el ID del alquiler a cancelar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Alquiler alquiler = escuela.getRepoAlquileres().buscarPorId(id);
            alquiler.cancelarAlquiler();

        }
        catch (Exception e)
        {
            System.out.println("⚠️ Error al cancelar el alquiler " + e.getMessage());
        }
    }

    public void cancelarReserva()
    {
        try
        {
            if (escuela.getRepoReservas().getTodos().isEmpty())
            {
                System.out.println("⚠️ No hay reservas cargadas.");
                return;
            }

            System.out.print("Ingrese el id de la reserva a cancelar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Reserva reserva = escuela.getRepoReservas().buscarPorId(id);

            if (reserva == null)
            {
                System.out.println("⚠️ No existe una reserva con ese id.");
                return;
            }

            reserva.cancelarReserva();

        }
        catch (Exception e)
        {
            System.out.println("Error al cancelar la reserva " + e.getMessage());
        }

    }

    public void mostrarReservas()
    {
        if (escuela.getRepoReservas().getTodos().isEmpty())
        {
            System.out.println("⚠️ No se encuentrar reservas registradas.");
            return;
        }

        System.out.println("════════════ MOSTRAR RESERVAS ════════════");
        System.out.println("1) MOSTRAR SOLO ACTIVAS");
        System.out.println("2) MOSTRAR SOLO FINALIZADAS");
        System.out.println("3) MOSTRAR TODAS");
        System.out.println("Seleccione una opción: ");

        int opcion;

        try
        {
            opcion = Integer.parseInt(scanner.nextLine());
        }
        catch (NumberFormatException e)
        {
            System.out.println("⚠️ Debes ingresar un número válido.");
            return;
        }

        System.out.println("--- DEBUG: VERIFICANDO ESTADO INICIAL ---");
        escuela.getRepoReservas().getTodos().forEach(reserva ->
        {
            System.out.println("ID Reserva: " + reserva.getIdReserva() + " | Activa: " + reserva.isEstaActiva());
        });
        System.out.println("------------------------------------------");

        System.out.println("════════════ RESULTADO ════════════");
        switch (opcion)
        {
            case 1:
                System.out.println("RESERVAS ACTIVAS: ");
                escuela.getRepoReservas().getTodos().stream()
                        .filter(reserva -> reserva.isEstaActiva())
                        .forEach(Reserva::mostrarReservaMejorada);
                break;

            case 2:
                System.out.println("RESERVAS FINALIZADAS: ");
                escuela.getRepoReservas().getTodos().stream()
                        .filter(reserva -> !reserva.isEstaActiva())
                        .forEach(Reserva::mostrarReservaMejorada);
                break;

            case 3:
                System.out.println("TODOS LAS RESERVAS: ");
                escuela.getRepoReservas().getTodos().forEach(Reserva::mostrarReservaMejorada);
                break;

            default:
                System.out.println("Opción no válida.");

        }
        System.out.println("═══════════════════════════════════════════════\n");
    }

    private MetodoPago seleccionarMetodoPago() throws IllegalArgumentException, InputMismatchException
    {
        System.out.println("Seleccione el método de pago:");
        System.out.println("1. " + MetodoPago.EFECTIVO);
        System.out.println("2. " + MetodoPago.TARJETA);
        System.out.println("3. " + MetodoPago.TRANSFERENCIA);
        System.out.print("Ingrese una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        return switch (opcion)
        {
            case 1 -> MetodoPago.EFECTIVO;
            case 2 -> MetodoPago.TARJETA;
            case 3 -> MetodoPago.TRANSFERENCIA;
            default -> throw new IllegalArgumentException("Opción de pago inválida.");
        };
    }

    public void pagarUnaReserva()
    {
        try
        {
            System.out.println("PAGO DE RESERVA DE CLASE\n");
            System.out.print("Ingrese el ID de la reserva que desea pagar: ");
            int idReserva = scanner.nextInt();
            scanner.nextLine();

            MetodoPago metodo = seleccionarMetodoPago();

            escuela.pagarReserva(idReserva, metodo);

            System.out.println("✅ Pago de reserva: " + idReserva + " registrado exitosamente!");
        }
        catch (InputMismatchException e)
        {
            System.out.println("❌ Error: debes ingresar un número.");
            scanner.nextLine();
        }
        catch (IdNoEncontradoException | IllegalStateException | IllegalArgumentException e)
        {
            System.out.println("❌ Error al procesar el pago: " + e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("⚠️ Error inesperado: " + e.getMessage());
        }
    }

    public void pagarUnAlquiler()
    {
        try
        {
            System.out.println("PAGO DE ALQUILER DE EQUIPO\n");
            System.out.print("Ingrese el ID del alquiler que desea pagar: ");
            int idAlquiler = scanner.nextInt();
            scanner.nextLine();

            MetodoPago metodo = seleccionarMetodoPago();

            escuela.pagarAlquiler(idAlquiler, metodo);

            System.out.println("✅ Pago de alquiler: " + idAlquiler + " registrado exitosamente!");
        }
        catch (InputMismatchException e)
        {
            System.out.println("❌ Error: debes ingresar un número.");
            scanner.nextLine();
        }
        catch (IdNoEncontradoException | IllegalStateException | IllegalArgumentException e)
        {
            System.out.println("❌ Error al procesar el pago: " + e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("⚠️ Error inesperado: " + e.getMessage());
        }
    }

    public void chequearMorosidadAlumno()
    {
        System.out.println("CHEQUEO DE MOROSIDAD\n");

        try
        {
            System.out.print("Ingrese el ID del ALUMNO a chequear: ");
            int idAlumno = scanner.nextInt();
            scanner.nextLine();

            boolean esMoroso = escuela.chequearMorosidadAlumno(idAlumno);
            if (esMoroso)
            {
                System.out.println("El alumno ID " + idAlumno + " TIENE pagos pendientes vencidos.");
            }
            else
            {
                System.out.println("El alumno ID " + idAlumno + " está al día con sus pagos.");
            }
        }
        catch (InputMismatchException e)
        {
            System.out.println("❌ Error: debe ingresar un número.");
            scanner.nextLine();
        }
        catch (IdNoEncontradoException e)
        {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public void chequearMorosidadCliente()
    {
        System.out.println("CHEQUEO DE MOROSIDAD\n");

        try
        {
            System.out.print("Ingrese el ID del CLIENTE a chequear: ");
            int idCliente = scanner.nextInt();
            scanner.nextLine();

            boolean esMoroso = escuela.chequearMorosidadCliente(idCliente);
            if (esMoroso)
            {
                System.out.println("El cliente ID " + idCliente + " TIENE pagos pendientes vencidos.");
            }
            else
            {
                System.out.println("El cliente ID " + idCliente + " está al día con sus pagos.");
            }
        }
        catch (InputMismatchException e)
        {
            System.out.println("❌ Error: debe ingresar un número.");
            scanner.nextLine();
        }
        catch (IdNoEncontradoException e)
        {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public void mostrarAlumnosInscriptosEnClase()
    {
        System.out.println("MOSTRAR ALUMNOS INSCRIPTOS\n");

        try
        {
            System.out.println("Ingrese el ID de la clase para mostrar sus alumnos: ");
            int idClase = scanner.nextInt();
            scanner.nextLine();

            List<Alumno> arrAlumnos = escuela.mostrarAlumnosDeUnaClase(idClase);

            if (arrAlumnos.isEmpty())
            {
                System.out.println("La clase ID " + idClase + " existe, pero no tiene alumnos activos.");
            }
            else
            {
                for (Alumno alumno : arrAlumnos)
                {
                    System.out.println("  - " + alumno);
                }
            }
        }
        catch (IdNoEncontradoException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        catch (InputMismatchException e)
        {
            System.out.println("\nError: ingresa un valor correcto. " + e.getMessage());
            scanner.nextLine();
        }
        catch (Exception e)
        {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    public void grabarRepositoriosAjson()
    {
        JsonUtiles.grabarUnJson(escuela.toJSON(), "escuelaDeSurf.json");
    }
}
