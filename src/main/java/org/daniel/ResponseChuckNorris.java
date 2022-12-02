package org.daniel;


import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResponseChuckNorris {
    private List<String> categories;
    @JsonAlias("created_at")
    private String createdAt;
    private String value;
    @JsonAlias("icon_url")
    private String iconUrl;
    private String id;
    @JsonAlias("update_at")
    private String updateAt;
    private String url;
}
