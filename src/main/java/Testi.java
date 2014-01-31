import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;


@XmlRootElement
public class Testi {

    @XmlAttribute
    private boolean canRead;

    @XmlAttribute
    private boolean canWrite;

    @XmlValue
    private String value;
    
    @XmlElement(required=true)
    private String bar;
    
    @XmlElement(name="teti")
    private Teti teti;
    
    public static void main(String[] args) throws JsonMappingException {
    	ObjectMapper jsonMapper = new ObjectMapper();
        AnnotationIntrospector introspector = new AnnotationIntrospector.Pair(new JaxbAnnotationIntrospector(), 
        		new JacksonAnnotationIntrospector());
        jsonMapper.setAnnotationIntrospector(introspector);
        JsonSchema schema = jsonMapper.generateJsonSchema(Testi.class);
        if(Testi.class.getAnnotation(XmlRootElement.class)!=null
        		&& !Testi.class.getAnnotation(XmlRootElement.class).name().equals("##default"))
        	schema.getSchemaNode().put("name", Testi.class.getAnnotation(XmlRootElement.class).name());
        else if(Testi.class.getAnnotation(XmlType.class)!=null
        		&& !Testi.class.getAnnotation(XmlType.class).name().equals("##default"))
        	schema.getSchemaNode().put("name", Testi.class.getAnnotation(XmlType.class).name());
        else
        	schema.getSchemaNode().put("name", Testi.class.getSimpleName());
        String schemaJson = schema.toString();
    	System.out.println(schemaJson);
    }
}