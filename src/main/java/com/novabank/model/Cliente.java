package  com.novabank.model;

public class  Cliente  {
    private  Long  id;
    private static  Long  contadorId  =  1L;
    private String  nombre;
    private  String  apellidos;
    private  String dni;
    private  String  email;
    private  String  telefono;

    //  Constructor  que  usas  al crear  clientes  nuevos  en  memoria
    public Cliente(String  nombre,  String  apellidos,  String dni,  String  email,  String  telefono) {
        this.id =  contadorId++;
        this.nombre  =  nombre;
        this.apellidos  =  apellidos;
        this.dni  =  dni;
        this.email  = email;
        this.telefono =  telefono;
    }

    //  Nuevo constructor  para  cuando  viene  de la  BD
    public  Cliente(Long  id,  String nombre,  String  apellidos,  String  dni, String  email,  String  telefono)  {
        this.id  = id;
        this.nombre =  nombre;
        this.apellidos  =  apellidos;
        this.dni  =  dni;
        this.email  =  email;
        this.telefono  = telefono;
    }



    public void  setId(Long  id)  {
        this.id  =  id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDni() {
        return dni;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public Long getId() {
        return id;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
