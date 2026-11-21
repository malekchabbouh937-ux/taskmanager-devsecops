# Task Manager DevSecOps

Application Web de gestion des tâches (API REST) avec une chaîne CI/CD sécurisée.

## Architecture

GitHub -> Jenkins -> Build/Tests (Maven) -> SonarQube -> Dependency-Check
-> Docker build -> Trivy -> Docker Hub -> Kubernetes (Minikube) -> OWASP ZAP

## Technologies

- Java 17, Spring Boot, Maven, H2
- Git/GitHub, Jenkins, Docker, Minikube
- SonarQube, OWASP Dependency-Check, Trivy, OWASP ZAP

## Prérequis

- Ubuntu, Java 17, Maven 3.9, Git
- Docker, Minikube, Jenkins (phases suivantes)

## Exécution locale

    mvn test
    mvn spring-boot:run

L'application écoute sur le port 9090.

## API

- GET    /api/tasks
- GET    /api/tasks/{id}
- POST   /api/tasks
- PUT    /api/tasks/{id}
- DELETE /api/tasks/{id}

Exemple :

    curl -X POST http://localhost:9090/api/tasks -H "Content-Type: application/json" -d '{"title":"Test"}'

## Organisation des branches

- main : version stable
- develop : intégration
- feature/* : une branche par fonctionnalité

## Sécurité

- Validation des entrées (Bean Validation)
- Aucun secret dans le code (.gitignore)
- Authentification GitHub par clé SSH
- Dependabot et Secret scanning activés
- Protection de la branche main

## Docker, Kubernetes, Pipeline

À compléter dans les phases suivantes.


## Docker

    docker build -t taskmanager:1.0 .
    docker run -d --name taskmanager -p 9090:9090 taskmanager:1.0
    curl http://localhost:9090/api/tasks

## Kubernetes (Minikube)

    minikube start --driver=docker

    minikube image load taskmanager:1.1

    kubectl apply -f k8s/

    curl http://$(minikube ip):30090/api/tasks


## Image Docker Hub

Image publiée automatiquement par le pipeline Jenkins :

- Dépôt : https://hub.docker.com/r/chabbouha/taskmanager
- Tags : numéro de build (`1`, `2`, ...) et `latest`

Exécution :

    docker run -d -p 9090:9090 chabbouha/taskmanager:latest
    curl http://localhost:9090/api/tasks

## Pipeline CI/CD (Jenkins)

Le pipeline est défini dans le fichier `Jenkinsfile` versionné avec le projet.

| Étape | Description |
|---|---|
| Checkout | Récupération du code depuis GitHub (branche develop) |
| Build and Tests | `mvn clean verify` : compilation et tests unitaires |
| Docker Build | Construction de l'image à partir du Dockerfile |
| Docker Push | Publication sur Docker Hub |

Déclenchement : Poll SCM (vérification du dépôt toutes les 2 minutes).

### Gestion des secrets

Les identifiants Docker Hub sont stockés dans Jenkins Credentials (ID `dockerhub-creds`)
et injectés dans le pipeline avec `withCredentials`. Le mot de passe est transmis avec
`--password-stdin` : il n'apparaît ni dans le code ni dans les logs.
Un token d'accès (et non le mot de passe du compte) est utilisé, avec une date d'expiration.

### Sécurité (phases suivantes)

SonarQube, OWASP Dependency-Check, Trivy et OWASP ZAP seront ajoutés au pipeline.
