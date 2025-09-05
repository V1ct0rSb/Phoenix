# 🚀 Projeto de Estudo: Microservices com Spring Boot e RabbitMQ

Este repositório contém o código-fonte de um projeto de estudo focado em **arquitetura de microserviços**, utilizando **Java com Spring Boot** para os serviços e **RabbitMQ** como *message broker* para a comunicação assíncrona.

O projeto simula um **sistema de e-commerce simples**, dividido em dois serviços independentes:

- **Serviço Phoenix**: Responsável pela lógica de negócio principal, como gerenciamento de usuários e pedidos.  
- **Serviço Email**: Responsável por consumir eventos e enviar notificações por email.

---

## 🎯 Objetivo

O principal objetivo deste projeto é demonstrar na prática os seguintes conceitos:

✅ **Desacoplamento** – Serviços que operam de forma independente.  
✅ **Comunicação Assíncrona** – Uso de filas (RabbitMQ) para evitar bloqueios e dependências diretas.  
✅ **Resiliência** – O sistema continua operando mesmo que um dos serviços esteja temporariamente indisponível.  
✅ **Escalabilidade** – A arquitetura permite que os serviços sejam escalados de forma independente.  

---

## 🏛️ Arquitetura

A comunicação entre os serviços é feita de forma **assíncrona** através do **RabbitMQ**.  

- **Serviço Phoenix (Producer)**: Publica mensagens em filas sempre que um evento relevante ocorre (ex: novo cadastro, pedido criado).  
- **Serviço Email (Consumer)**: Escuta as filas e processa as mensagens para enviar notificações.  

