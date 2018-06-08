package silvana.com.manual.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

import silvana.com.manual.database.Clasificacion;
import silvana.com.manual.datos.ArriboAtleta;
import silvana.com.manual.datos.EstadoAtleta;

/**
 * Created by silvana-aguero on 30/05/2018.
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClasificacionMapper {

    ClasificacionMapper INSTANCE = Mappers.getMapper(ClasificacionMapper.class);

    ArriboAtleta toArriboAtleta(Clasificacion clasificacion);

    List<ArriboAtleta> toListArriboAtleta(List<Clasificacion> listClasificacion);

    EstadoAtleta toEstadoAtleta(Clasificacion clasificacion);

    List<EstadoAtleta> toListEstadoAtleta(List<Clasificacion> listClasificacion);

    @Mappings({
            @Mapping(target = "idCompetencia", source = "arriboAtleta.idCompetencia"),
            @Mapping(target = "dorsal", source = "arriboAtleta.dorsal"),
            @Mapping(target = "tiempo", source = "arriboAtleta.tiempo"),
            @Mapping(target = "malIngresado", expression = "java(true)"),
            @Mapping(target = "observacion", source = "estadoAtleta.comentario"),
            @Mapping(target = "estado", source = "estadoAtleta.estado"),
    })
    Clasificacion toClasificacion(ArriboAtleta arriboAtleta, EstadoAtleta estadoAtleta);


    @Mappings({
            @Mapping(target = "idCompetencia", source = "arriboAtleta.idCompetencia"),
            @Mapping(target = "dorsal", source = "arriboAtleta.dorsal"),
            @Mapping(target = "tiempo", source = "arriboAtleta.tiempo"),
            @Mapping(target = "malIngresado", expression = "java(false)")
    })
    Clasificacion toClasificacion(ArriboAtleta arriboAtleta);


}
