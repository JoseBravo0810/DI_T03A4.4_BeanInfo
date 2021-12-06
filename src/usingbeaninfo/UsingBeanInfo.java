package usingbeaninfo;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author jose
 */
public class UsingBeanInfo extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        // Creamos una etiqueta para decir al usuario que hacer
        Label lb = new Label("Haz click en MiControl para ver su informacion ");
        
        // Creamos un Bean de MiControl()
        MiControl mc = new MiControl();
        
        // Le damos un alto maximo de 100, y un ancho maximo de 100
        mc.setMaxHeight(mc.getPrefHeight());
        mc.setMaxWidth(mc.getPrefWidth());
        
        // Creamos un area de texto para poder usar los scroll y ver toda la informacion en pantalla
        TextArea info = new TextArea();
        
        // El area de texto no se podrá editar
        info.setEditable(false);
        
        // Asignamos una accion al hacer click en el componente
        mc.setOnAction(event -> {
            try
            {
                // Creamos un BeanInfo del componente
                BeanInfo beanInfo = Introspector.getBeanInfo(mc.getClass());
                // Creamos una cadena donde se almacenará la informacion
                String sInfo = "";

                // Añadimos el nombre del componente a analizar
                sInfo += "Informacion del Bean " + mc.getClass().getName() + "\n\n";

                // Propiedades
                sInfo += "Las propiedades del Bean son:\n\n";
                for(PropertyDescriptor pd: beanInfo.getPropertyDescriptors())
                {
                    Class c = pd.getPropertyType();
                    Method write = pd.getWriteMethod();
                    Method read = pd.getReadMethod();

                    sInfo += "\tNombre de la propiedad: " + pd.getDisplayName() + "\n" +
                            "\tTipo: " + c.getTypeName() + "\n" +
                            "\tMetodo para escribir (setear) la propiedad: " + (write != null ? write.getName():" - ") + "\n" +
                            "\tMetodo para leer la propiedad: " + (read != null ? read.getName():" - ") + "\n\n";
                }
                
                // Metodos
                sInfo += "Los metodos del Bean son:\n\n";
                for(MethodDescriptor md: beanInfo.getMethodDescriptors())
                {
                    Method m = md.getMethod();

                    sInfo += "\tNombre del metodo: " + m.getName() + "\n" +
                            "\tNumero de parametros: " + m.getParameterCount() + "\n" +
                            "\tTipos de los parametros: \n";

                    for(Class c: m.getParameterTypes())
                    {
                        sInfo += "\t\t" + c.getTypeName() + "\n";
                    }

                    sInfo += (m.isBridge()? "\tEl metodo es un metodo bridge\n" : "") +
                            (m.isDefault() ? "\tEl metodo es un metodo por defecto\n" : "") +
                            (m.isSynthetic() ? "\tEl metodo es sintetico\n" : "") +
                            (m.isVarArgs() ? "\tEl numero de argumentos del metodo es variable\n" : "") +
                            "\tDescripcion generica del metodo: " + m.toGenericString() + "\n" + 
                            "\tDescripcion del metodo: " + m.toString() + "\n\n";
                }

                // Mostramos la infomracion en la consola y en el TextArea
                System.out.println(sInfo);
                info.setText(sInfo);
            }
            catch(java.beans.IntrospectionException ie)
            {
                //System.out.println(ie.getMessage());
                System.out.println("Ha ocurrido un error");
                info.setText("Ha ocurrido un error");
            }
        });
        
        // Creamos un HBox que tendra la etiqueta y el componente
        HBox header = new HBox();
        // Lo alineamos al centro de la escena
        header.setAlignment(Pos.CENTER);
        // Dejamos espacio entre componentes de 10px
        header.setSpacing(10);
        // Le damos un padding para que no quede pegado al borde
        header.setPadding(new Insets(10, 0, 0, 0));
        // Añadimos la etiqueta y el componente
        header.getChildren().addAll(lb, mc);
        
        // Creamos un VBox que sera el nodo raiz de la escena
        VBox root = new VBox();
        // Lo alinemos al centro
        root.setAlignment(Pos.CENTER);
        // Damos espacio de 10px entre los componentes del VBox
        root.setSpacing(10);
        // Añadimos el VBox y el TextArea
        root.getChildren().addAll(header, info);
        
        // Creamos la escena con el nodo raiz y dandole un ancho y alto
        Scene scene = new Scene(root, 1080, 640);
        
        // Asignamos el alto del TextArea para que sea de alto como la escena menos el alto del compoente
        info.setPrefHeight(scene.getHeight() - mc.getHeight());
        
        // Hacemos que el escenario no pueda redimensionarse
        primaryStage.setResizable(false);
        
        // Le damos titulo a la ventana, le asignamos la escena y la hacemos visible
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
