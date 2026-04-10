package hiddencore.ddasum.backend.web.dto;

import hiddencore.ddasum.backend.domain.Location;
import hiddencore.ddasum.backend.domain.Location.RoomType;
import hiddencore.ddasum.backend.domain.Location.RoomGenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LocationDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LocationCardDto {

        // 병실 기본 정보
        private Long locationId;
        private String building;
        private Integer floor;
        private String room;
        private RoomType roomType;
        private RoomGenderType roomGenderType;
        private Boolean isOccupied;
        private Integer roomCapacity;

        

        public static LocationCardDto from (Location location) {
            
            return LocationCardDto.builder()
                .locationId(location.getLocationId())
                .building(location.getBuilding())
                .floor(location.getFloor())
                .room(location.getRoom())
                .roomType(location.getRoomType())
                .roomGenderType(location.getRoomGenderType())
                .isOccupied(location.getIsOccupied())
                .roomCapacity(location.getRoomCapacity())
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RoomPatientCountDto {
        private String building;
        private Integer floor;
        private String room;
        private RoomType roomType;
        private RoomGenderType roomGenderType;
        private Long locationCount;
        private Long patientCount;
    }

    // @Getter
    // @NoArgsConstructor
    // @AllArgsConstructor
    // @Builder
    // public static class RoomSummaryDto {
    //     private String building;
    //     private Integer floor;
    //     private String room;
    //     private RoomType roomType;
    //     private RoomGenderType roomGenderType;
    //     private Long locationCount;
    //     private Long patientCount;
    // }
}
