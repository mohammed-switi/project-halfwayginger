package edu.bethlehem.scinexus.ResearchPaper;

public class ResearchPaperNotFoundException extends RuntimeException {

  public ResearchPaperNotFoundException(Long id) {
    super("Could not find ResearchPaper" + id);
  }
}
