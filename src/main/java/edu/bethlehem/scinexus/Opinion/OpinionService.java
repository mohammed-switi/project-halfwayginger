package edu.bethlehem.scinexus.Opinion;

import edu.bethlehem.scinexus.DatabaseLoading.DataLoader;
import edu.bethlehem.scinexus.JPARepository.OpinionRepository;
import edu.bethlehem.scinexus.Journal.JournalNotFoundException;
import edu.bethlehem.scinexus.JPARepository.JournalRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class OpinionService {

    private final JournalRepository journalRepository;
    private final OpinionRepository opinionRepository;
    private final OpinionModelAssembler assembler;
    Logger logger = LoggerFactory.getLogger(DataLoader.class);

    public Opinion convertOpinionDtoToOpinionEntity(OpinionDTO opinionDTO) {

        return Opinion.builder()
                .content(opinionDTO.getContent())
                .journal(journalRepository.findById(opinionDTO.getJournalId())
                        .orElseThrow(() -> new JournalNotFoundException(opinionDTO.getJournalId())))
                .build();

    }

    public EntityModel<Opinion> getOneOpinion(Long opinionId) {
        logger.trace("Finding Opinion by ID");
        Opinion opinion = opinionRepository.findById(opinionId)
                .orElseThrow(() -> new OpinionNotFoundException(opinionId, HttpStatus.NOT_FOUND));
        return assembler.toModel(opinion);
    }

    public List<EntityModel<Opinion>> getAllOpinions() {
        logger.trace("Finding All Opinions");
        return opinionRepository.findAll().stream().map(assembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Opinion> postOpinion(OpinionDTO newOpinionDTO) {
        logger.trace("Posting New Opinion");
        Opinion newOpinion = convertOpinionDtoToOpinionEntity(newOpinionDTO);
        return assembler.toModel(opinionRepository.save(newOpinion));
    }

    public EntityModel<Opinion> updateOpinion(Long id, OpinionDTO opinionDTO) {
        logger.trace("Updating Opinion");
        return opinionRepository.findById(id)
                .map(opinion -> {
                    opinion.setContent(opinionDTO.getContent());
                    return assembler.toModel(opinionRepository.save(opinion));
                })
                .orElseThrow(() -> new JournalNotFoundException(id));

    }

    public EntityModel<Opinion> updateOpinionPartially(Long opinionId, OpinionPatchDTO opinionPatchDTO) {
        logger.trace("Partially Updating Opinion");
        Opinion opinion = opinionRepository.findById(opinionId)
                .orElseThrow(() -> new OpinionNotFoundException(opinionId, HttpStatus.NOT_FOUND));

        if (opinionPatchDTO.getContent() != null)
            opinion.setContent(opinionPatchDTO.getContent());

        return assembler.toModel(opinionRepository.save(opinion));

    }

    public void deleteOpinion(Long id) {
        logger.trace("Deleting Opinion");
        opinionRepository.deleteById(id);

    }

}
