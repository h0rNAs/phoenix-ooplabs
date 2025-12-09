package ru.ssau.tk.phoenix.ooplabs.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.ssau.tk.phoenix.ooplabs.functions.Point;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = false
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleFunction.class, name = "SIMPLE"),
        @JsonSubTypes.Type(value = TabulatedFunction.class, name = "TABULATED"),
        @JsonSubTypes.Type(value = CompositeFunction.class, name = "COMPOSITE")
})
public abstract class FunctionDefinition implements Serializable {
    private static long serialVersionUID;
    protected Point[] points;

    public Point[] getPoints() {
        return points;
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }
}
