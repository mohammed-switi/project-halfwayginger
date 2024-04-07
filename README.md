`<a name="readme-top"></a>`

[LinkedIn][linkedin-url]

<!-- PROJECT LOGO -->

<br />
<div align="center">
<!--   <a href="https://github.com/othneildrew/Best-README-Template">
<!--     <img src="images/logo.png" alt="Logo" width="80" height="80"> -->
<!--   </a> -->
  <h3 align="center">SciNexus Project Readme</h3>

<p align="center">
    <br />
<!--     <a href="https://github.com/othneildrew/Best-README-Template"><strong>Explore the docs »</strong></a>
    <br /> -->
    <br />
  </p>
</div>

<!-- TABLE OF CONTENTS -->

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#features">Features</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## About The Project

SciNexus is a pioneering social media platform designed specifically for researchers and institutions passionate about advancing scientific knowledge. Our platform serves as a dynamic hub where scientists can communicate, collaborate, and exchange ideas seamlessly. From sharing groundbreaking discoveries to publishing research papers, SciNexus empowers scientists to connect with peers worldwide, fostering innovation and accelerating scientific progress.

At SciNexus, we prioritize security and confidentiality, ensuring that sensitive data and research findings remain protected at all times. With robust security measures in place, researchers can engage in discussions and share insights with confidence, knowing their work is safeguarded.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

##Documentation

You can access the API documentation through the Swagger-Ui [host]/swagger-ui/index.html

### Built With

- [Spring][spring-url]
- [SpringBoot][springboot-url]
- [Java][java-url]
- [MySQL][mysql-url]
- [JavaScript][javascript-url]
- [MongoDB][mongodb-url]
- [Docker][docker-url]
- [HTML][html-url]
- [CSS][css-url]
- [Google][google-url]
- [Github][github-url]
- [Bootstrap][Bootstrap-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->

## Getting Started

This is a guide to help you set up your project locally. Follow these steps to get a local copy up and running :

### Prerequisites

Runnung this project with full functionalities will require:

- [Docker](https://www.docker.com/)
- [MySQL](https://www.mysql.com/)
- [MongoDB](https://www.mongodb.com/)
- [npm (Node Package Manager)](https://www.npmjs.com/)
- [Maildev](https://github.com/maildev/maildev)

### Installation

1. Clone the repo

```sh
git clone https://github.com/S24-SWER313/project-halfwayginger.git
```

2. Install NPM packages

```sh
npm install maildev
```

4. Start The Docker Server

```sh
docker-compose up -d
```

5. Start The Mail Service

```sh
npx maildev
```

4. Customize your Database Configuration in `application-properties`, This is Default:

```sh
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.datasource.username=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.datasource.url=jdbc:mysql://localhost:3306/scinexusdatabase
spring.datasource.password=1234
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->

<!-- ROADMAP -->

## Features

- Three kinds of media (Research Paper, Articles, and Posts)
- Two different users (Organization and Academic)
- Chatting (NoSQL Database)
- Interactions
- Opinions
- High security measuers
- Bidierctional user linking (i.e. follow and follow back mechanism)
- Push-notifications
- And many more.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->

## Contributing

Although this project is not open-source, we value collaboration and welcome contributions from interested individuals. If you would like to suggest updates, enhancements, or fixes to the code base, we encourage you to engage with us in the following ways:

1. **Direct Communication** : Reach out to the project publishers or maintainers directly to discuss your ideas or proposed changes.
2. **Issue Tracking** : If you identify a bug, have an enhancement suggestion, or want to discuss any aspect of the project, please submit an issue in the designated section. We appreciate detailed descriptions and, if possible, proposed solutions or approaches.
3. **Feature Requests** : If you have ideas for new features or improvements that you believe would benefit the project, please share them with us. We evaluate all suggestions and prioritize them based on their potential impact and alignment with the project's goals.
4. **Code Contributions** : While we manage the project internally, there may be opportunities for external collaborators to contribute code or participate in specific tasks. If you're interested in this, please let us know, and we can discuss the details.

Please note that all contributions will be reviewed and may be subject to approval by the project maintainers. We aim to maintain a high standard of quality and coherence in our project, and we appreciate your understanding and cooperation in this regard.

Thank you for your interest in contributing to our project!

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- LICENSE -->

## License

Proprietary License

Copyright (c) 2025 SciNexus

All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are not permitted. This software and associated documentation files (the "Software") may only be used under the terms and conditions defined by SciNexus Co. Unauthorized reproduction, distribution, or use of the Software or any part thereof is strictly prohibited and may result in severe civil and criminal penalties.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->

## Contact

- Mohammed Sowaity [Email] [[GitHub](https://github.com/mohammed-switi)]
- Obada Tahboub [[Email](obadatahboub20@gmail.com)] [[GitHub](https://github.com/ObadaTah)]

Project Link: [S24-SWER313/project-halfwayginger: project-halfwayginger created by GitHub Classroom](https://github.com/S24-SWER313/project-halfwayginger)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ACKNOWLEDGMENTS -->

## Acknowledgments

We extend our heartfelt thanks to the following individuals, organizations, and communities for their invaluable contributions, support, and inspiration:

### Project Contributors

- **Development Team** : A special acknowledgment to the core development team for their dedication, expertise, and collaborative spirit in building and enhancing this project.
- **Testers and QA Team** : We appreciate the efforts of the testing and quality assurance team for identifying issues, ensuring functionality, and improving the overall quality of the project.

### Peer Reviewers and Advisors

- **Technical Reviewers** : Special thanks to the technical reviewers who have provided valuable feedback, suggestions, and constructive criticism to improve the code quality and project architecture.
- **Domain Experts** : We acknowledge the contributions of domain experts who have provided insights, guidance, and expertise in relevant fields, enriching the project's content and functionality.

### Users and Supporters

- **Early Adopters** : Thank you to the early adopters and beta testers who have provided feedback, reported issues, and helped us refine and optimize the project before its official release.
- **Community Supporters** : We appreciate the ongoing support, engagement, and advocacy from our user community, which motivates us to continuously improve and innovate.

### Funding and Sponsorship

- **Financial Supporters** : We acknowledge the financial support and sponsorship provided byno one which has enabled us to invest in development, maintenance, and community engagement.
- **Grants and Awards** : We are grateful for the grants, awards, and recognition received from no one too which have supported our research, development, and outreach efforts.
- I dought that there is someone will read this so if you have read this just send me a hi on my [email](obadatahboub20@gmail.com)

### Family and Friends

- **Family Members** : Last but certainly not least, we would like to thank our families for their unwavering support, understanding, and patience throughout the demanding phases of the project. TBH: my family were like "You are always on the computer that will not help you in anything" so...
- **Friends and Colleagues** : Thank you to our friends and colleagues for their encouragement, moral support, and occasional distractions that have helped us maintain balance and perspective during the project's lifecycle.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->

<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[spring-url]: https://spring.io/
[spring-icon]: https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white
[springboot-url]: https://spring.io/projects/spring-boot
[springboot-icon]: https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white
[java-url]: https://www.java.com/en/
[java-icon]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[mysql-url]: https://www.mysql.com/
[mysql-icon]: https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white
[mongodb-url]: https://www.mongodb.com/
[mongodb-icon]: https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white
[docker-url]: https://www.docker.com/
[docker-icon]: https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=fff&style=for-the-badge
[html-url]: https://html.spec.whatwg.org/multipage/
[html-icon]: https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white
[css-url]: https://www.w3.org/Style/CSS/Overview.en.html
[css-icon]: https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white
[javascript-url]: https://www.javascript.com/
[javascript-icon]: https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black
[thymeleaf-url]: https://www.thymeleaf.org/
[thymeleaf-icon]: https://img.shields.io/badge/Thymeleaf-005F0F?logo=thymeleaf&logoColor=fff&style=for-the-badge
[google-url]: https://cloud.google.com/apigee/docs/api-platform/security/oauth/oauth-introduction
[google-icon]: https://img.shields.io/badge/Google%20Authenticator-4285F4?logo=googleauthenticator&logoColor=fff&style=for-the-badge
[github-url]: https://img.shields.io/badge/Google%20Cloud-4285F4?logo=googlecloud&logoColor=fff&style=for-the-badge
[github-icon]: https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[contributors-url]: https://github.com/github_username/repo_name/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo_name.svg?style=for-the-badge
[forks-url]: https://github.com/github_username/repo_name/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge
[stars-url]: https://github.com/github_username/repo_name/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge
[issues-url]: https://github.com/github_username/repo_name/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo_name.svg?style=for-the-badge
[license-url]: https://github.com/github_username/repo_name/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/linkedin_username
[product-screenshot]: images/screenshot.png
[Next.js]: https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Next-url]: https://nextjs.org/
[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[Vue.js]: https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D
[Vue-url]: https://vuejs.org/
[Angular.io]: https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white
[Angular-url]: https://angular.io/
[Svelte.dev]: https://img.shields.io/badge/Svelte-4A4A55?style=for-the-badge&logo=svelte&logoColor=FF3E00
[Svelte-url]: https://svelte.dev/
[Laravel.com]: https://img.shields.io/badge/Laravel-FF2D20?style=for-the-badge&logo=laravel&logoColor=white
[Laravel-url]: https://laravel.com
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[JQuery.com]: https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white
[JQuery-url]: https://jquery.com
[Laravel-url]: https://laravel.com
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[JQuery.com]: https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white
[JQuery-url]: https://jquery.com
