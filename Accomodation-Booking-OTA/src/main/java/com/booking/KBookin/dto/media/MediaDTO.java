package com.booking.KBookin.dto.media;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MediaDTO implements Serializable {
        private  Long id;
        private  String url;
}
