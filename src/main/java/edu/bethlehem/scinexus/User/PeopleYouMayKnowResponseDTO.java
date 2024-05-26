package edu.bethlehem.scinexus.User;


import edu.bethlehem.scinexus.Media.Media;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Builder
public class PeopleYouMayKnowResponseDTO {

    private Long id;
    private String name;
    private Integer sharedSkills;
    private Media profilePicture;
    private boolean accepted;

}
