package Enumeradores;

public enum NombreEquipo
{
    HOME_DECO_X50("Home Deco x50", 150),
    TABLA_DE_SURF("Tabla de Surf", 100),
    TRAJE_DE_NEOPRENE("Traje de Neoprene", 75),
    BODYBOARD("Bodyboard", 50),
    PATAS_DE_RANA("Patas de Rana", 25);

    private final String nombreEquipo;
    private final int valorDelEquipoPorDia;

    NombreEquipo(String nombreEquipo,  int valorDelEquipoPorDia)
    {
        this.nombreEquipo = nombreEquipo;
        this.valorDelEquipoPorDia = valorDelEquipoPorDia;
    }

    public String getNombreEquipo()
    {
        return nombreEquipo;
    }

    public int getValorDelEquipoPorDia()
    {
        return valorDelEquipoPorDia;
    }
}
