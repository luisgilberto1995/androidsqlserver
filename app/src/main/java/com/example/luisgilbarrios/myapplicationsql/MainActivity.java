package com.example.luisgilbarrios.myapplicationsql;

import android.app.Activity;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    //Se declara la variable del boton
    //Tambien se declara la variable de la tabla
    //Estos dos objetos ya existen en la interfaz, estas variables solo son para conectarlas
    Button boton;
    String dato = "vacio";
    TableLayout tabla;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy  policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //--------------------------
        //----------INICIA CODIGO--------
        //-------------------------------
        //Amarramos la tabla de la interfaz a nuestra variable tabla
        tabla = (TableLayout)findViewById(R.id.tabla1);
        //Amarramos el boton de la interfaz a nuestra variable boton
        boton = (Button)findViewById(R.id.button);
        //Declaramos el onClickListener que es el que permite agregar acciones al 'clic' del boton
        boton.setOnClickListener(new View.OnClickListener() {
            //Esta palabra clave override significa que vamos a reescribir el metodo que trae
            //el objeto boton, asi no usa su accion por defecto, sino una que queremos declarar nosotros
            @Override
            public void onClick(View view) {
                //Llamada al metodo que esta abajo
                metodoBoton();
            }
        });
    }

    public void metodoBoton()
    {
        try {
            //Aqui se importa la libreria para la conexion que esta en la carpeta libs
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //ip, usuario, tabla y contrasena del servidor sql server
            String ipdir = "192.168.0.10:1433";
            String databasename = "testdb";
            String username = "luisbase";
            String pass="perro123";
            //Se arma la cadena de conexion
            String conexionstr = "jdbc:jtds:sqlserver://"+ipdir+";databaseName="+databasename+";user="+username+";password="+pass;
            //Conectamos a la base
            Connection conexion = DriverManager.getConnection(conexionstr);
            //Declaramos un objeto statement, este representa un query
            Statement statement = conexion.createStatement();
            //Definimos y ejecutamos el query, y lo capturamos en un ResultSet
            ResultSet resultSet = statement.executeQuery("select * from empleados");
            //La funcion .next() corre el indice en el que se encuentran los resultados del query
            //Y retorna un true o false si aun existen datos
            //Para ver el primer dato hay que hacer .next(), aunque sea el primero
            while(resultSet.next())
            {
                //Creamos un objeto fila para la tabla, y le pedimos que adopte la forma de un xml
                //que se encuentar en la carpeta layout
                //esto se ve en el .inflate(R.layout.attrib_row, donde attrib_row es el xml que le da forma a la tabla
                TableRow row = (TableRow)LayoutInflater.from(MainActivity.this).inflate(R.layout.attrib_row, null);
                //El xml tiene 2 columnas, una attrib_name que muestra los textos en negrita
                // y la otra con texto normal, que se llama attrib_value
                ((TextView)row.findViewById(R.id.attrib_name)).setText("NOMBRE:     ");
                //resultSet.getString(nombre de la columna de la tabla o indice)
                ((TextView)row.findViewById(R.id.attrib_value)).setText(resultSet.getString("nombre"));
                //agregamos la fila con 2 columnas a la tabla
                tabla.addView(row);
            }

            //se cierra la conexion
            conexion.close();
        } catch (ClassNotFoundException e) {
            //excepcion si no se encuentra la clase del driver para conectar a sql
            e.printStackTrace();
        } catch (SQLException e) {
            //excepcion si truena la conexion o el query a la base de datos
            e.printStackTrace();
        }

    }
}
