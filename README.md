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

