// package edu.bethlehem.scinexus;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Random;

// import org.ajbrown.namemachine.Name;
// import org.ajbrown.namemachine.NameGenerator;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import edu.bethlehem.scinexus.Academic.AcademicRepository;
// import edu.bethlehem.scinexus.Academic.Position;
// import edu.bethlehem.scinexus.Article.Article;
// import edu.bethlehem.scinexus.Article.ArticleRepository;
// import edu.bethlehem.scinexus.Media.Media;
// import edu.bethlehem.scinexus.Media.MediaRepository;
// import edu.bethlehem.scinexus.Organization.Organization;
// import edu.bethlehem.scinexus.Organization.OrganizationRepository;
// import edu.bethlehem.scinexus.Post.Post;
// import edu.bethlehem.scinexus.Post.PostRepository;
// import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
// import edu.bethlehem.scinexus.ResearchPaper.ResearchPaperRepository;
// import edu.bethlehem.scinexus.Academic.Academic;
// import edu.bethlehem.scinexus.Media.MediaType;

// @Configuration
// class LoadDatabase {

// // private static final Logger log =
// // LoggerFactory.getLogger(LoadDatabase.class);

// @Bean
// CommandLineRunner initDatabase(AcademicRepository academicRepository,
// ArticleRepository articleRepository,
// MediaRepository mediaRepository, PostRepository postRepository,
// OrganizationRepository organizationRepository,
// ResearchPaperRepository researchPaperRepository) {

// return args -> {
// NameGenerator generator = new NameGenerator();
// Random random = new Random();
// Integer bound = 50;

// // generate academics
// List<Name> names = generator.generateNames(bound);
// ArrayList<Academic> academics = new ArrayList<Academic>();
// for (Name name : names) {
// academics.add(new Academic(name.toString(), name.getFirstName(),
// "pass1234" + name.getFirstName(), name.getLastName() + name.getFirstName() +
// "@bethlehem.edu"));

// }
// academicRepository.saveAll(academics);

// // generate articles
// ArrayList<Article> articles = new ArrayList<Article>();
// for (int i = 0; i < bound; i++) {
// Academic academic = academics.get(random.nextInt(academics.size()));
// articles.add(new Article("title" + academic.getName(), "description" +
// academic.getEmail(), "subject", academic,
// "content"));
// }
// articleRepository.saveAll(articles);

// // generate Organizations
// ArrayList<Organization> organizations = new ArrayList<Organization>();
// for (int i = 0; i < bound; i++) {
// Academic academic = academics.get(random.nextInt(academics.size()));
// organizations.add(new Organization("name" + academic.getName(), "username" +
// academic.getName(),
// "password" + academic.getName(), "email" + academic.getName(), "type"));
// if (random.nextInt(3) == 0) {
// organizations.get(i).setVerified(true);
// }
// if (random.nextInt(3) == 0) {
// Integer rand = random.nextInt(academics.size());
// Academic academicT = academics.get(rand);
// // academicT.setOrganization(organizations.get(i));
// academicT.setBadge("badge" + academicT.getName());
// academicT.setPosition(Position.values()[random.nextInt(Position.values().length)]);
// academicRepository.save(academicT);
// }
// }
// organizationRepository.saveAll(organizations);

// // generate posts
// ArrayList<Post> posts = new ArrayList<Post>();
// for (int i = 0; i < bound; i++) {
// Academic academic = academics.get(random.nextInt(academics.size()));
// posts.add(new Post("Content" + academic.getName(), academic));
// }
// postRepository.saveAll(posts);

// // generate ResearchPapers
// ArrayList<ResearchPaper> researchPapers = new ArrayList<ResearchPaper>();
// for (int i = 0; i < bound; i++) {
// Academic academic = academics.get(random.nextInt(academics.size()));
// researchPapers
// .add(new ResearchPaper("Title" + academic.getName(), "Description" +
// academic.getEmail(), "Subject",
// academic));
// }
// researchPaperRepository.saveAll(researchPapers);

// // generate media
// // ArrayList<Media> media = new ArrayList<Media>();
// // for (int i = 0; i < bound * 5; i++) {
// // Media media1 = new Media(MediaType.IMAGE, "url" + i);

// // switch (random.nextInt(4)) {
// // case 0:

// // media1.setOwnerPost(posts.get(random.nextInt(posts.size())));

// // case 1:

// // media1.setOwnerArticle(articles.get(random.nextInt(articles.size())));

// // case 2:
// //
// media1.setOwnerResearchPaper(researchPapers.get(random.nextInt(researchPapers.size())));
// // case 3:
// // Integer rand = random.nextInt(academics.size());
// // Academic academicT = academics.get(rand);
// // if (media1.getOwnerPost() == null && media1.getOwnerArticle() == null
// // && media1.getOwnerResearchPaper() == null) {
// // academicT.setProfilePicture(media1);
// // academicRepository.save(academicT);
// // }
// // case 4:
// // Integer rand1 = random.nextInt(academics.size());
// // Academic academicT1 = academics.get(rand1);
// // if (media1.getOwnerPost() == null && media1.getOwnerArticle() == null
// // && media1.getOwnerResearchPaper() == null) {

// // academicT1.setProfileCover(media1);
// // academicRepository.save(academicT1);
// // }

// // }
// // media.add(media1);
// // }
// // mediaRepository.saveAll(media);
// System.out.println("Preloading ");

// };
// }
// }