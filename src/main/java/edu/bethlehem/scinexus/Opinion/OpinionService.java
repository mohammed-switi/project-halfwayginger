package edu.bethlehem.scinexus.Opinion;


import edu.bethlehem.scinexus.Journal.JournalNotFoundException;
import edu.bethlehem.scinexus.Journal.JournalRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
@Builder
public class OpinionService {

    private final JournalRepository journalRepository;
    private final OpinionRepository opinionRepository;
    private final OpinionModelAssembler assembler;


    public Opinion convertOpinionDtoToOpinionEntity(OpinionDTO opinionDTO){

        return Opinion.builder()
                .content(opinionDTO.getContent())
                .journal(journalRepository.findById(opinionDTO.getJournalId())
                                                                .orElseThrow(() -> new JournalNotFoundException(opinionDTO.getJournalId())))
                .createdAt(new Date(System.currentTimeMillis()))
                .build();

    }


    public EntityModel<Opinion> getOneOpinion(Long opinionId){
        Opinion opinion = opinionRepository.findById(opinionId)
                .orElseThrow(() -> new OpinionNotFoundException(opinionId, HttpStatus.NOT_FOUND));
        return assembler.toModel(opinion);
    }

    public List<EntityModel<Opinion>> getAllOpinions(){

        return opinionRepository.findAll().stream().map(assembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Opinion> postOpinion(OpinionDTO newOpinionDTO){
        Opinion newOpinion=convertOpinionDtoToOpinionEntity(newOpinionDTO);
        return assembler.toModel(opinionRepository.save(newOpinion));
    }

    public EntityModel<Opinion> updateOpinion(Long id,OpinionDTO opinionDTO){

        return opinionRepository.findById(id)
                .map(opinion -> {
                    opinion.setContent(opinionDTO.getContent());
                    return assembler.toModel(opinionRepository.save(opinion));
                })
                .orElseThrow(() -> new JournalNotFoundException(id));


    }

    public EntityModel<Opinion> updateOpinionPartially(Long opinionId, OpinionPatchDTO opinionPatchDTO){

        Opinion opinion = opinionRepository.findById(opinionId)
                .orElseThrow(() -> new OpinionNotFoundException(opinionId,HttpStatus.NOT_FOUND));

        if (opinionPatchDTO.getContent() != null)
            opinion.setContent(opinionPatchDTO.getContent());

        return assembler.toModel(opinionRepository.save(opinion));

    }

    public void deleteOpinion(Long id){

        opinionRepository.deleteById(id);

    }

}


