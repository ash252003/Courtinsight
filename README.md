# CourtInsight

CourtInsight is an AI-powered legal awareness platform designed to make Indian criminal law accessible to ordinary citizens. The platform focuses on the **Bharatiya Nyaya Sanhita (BNS) 2023**, which replaced the 163-year-old Indian Penal Code on July 1, 2024, and completely restructured criminal sections. CourtInsight simplifies complex legal language and provides clear explanations in English and Hindi through an AI-powered chat interface.

---

## Problem Statement

India’s judicial system produces thousands of legal documents every day, but most citizens cannot easily access or understand them due to complex legal terminology and fragmented platforms. With the introduction of **BNS 2023**, the problem became even more severe because all criminal sections were renumbered and reorganized.

Our survey found that most citizens rated legal information as **“not accessible”** and relied on unreliable sources such as search engines or social media. Existing platforms like Indian Kanoon, eCourts, or SCC Online do not provide AI-generated explanations of BNS in simple language for common citizens.

---

## Solution Overview

CourtInsight provides an AI-powered platform that explains BNS provisions in simple, understandable language. The system allows users to ask questions about legal sections and receive accurate explanations through a conversational interface.

The goal is to improve **legal awareness, accessibility, and transparency** in the Indian justice system.

---

## BNS Explanation Module (Live)

The deployed system uses a **Retrieval-Augmented Generation (RAG)** pipeline.

Deployment:  
https://huggingface.co/spaces/Ayushi054/BNS-Legal-Chat-v2

The process works in three stages:

### 1. Query Translation
Hindi queries are translated into English using the **Google Translate API** to ensure consistent semantic processing.

### 2. Semantic Retrieval
The **all-MiniLM-L6-v2** sentence transformer performs semantic search across all **358 BNS sections** and retrieves the most relevant sections based on meaning rather than simple keyword matching.

### 3. AI Response Generation
The **Gemma 2 9B** language model generates grounded answers using only the retrieved BNS text. This approach reduces hallucination and prevents incorrect section references.

Additionally, **Phi-2 and Llama 3.2 3B models** were fine-tuned using **3,000 custom BNS question-answer pairs**, achieving an evaluation loss of **0.1489**, improving accuracy for legal explanations.

---

## Case Summarization Module (Proposed)

Court administrators will be able to upload legal case documents directly to the platform.

Live prototype:  
https://huggingface.co/spaces/Ayushi054/summaraization

The **distilbart-cnn-12-6 summarization model** will generate concise summaries of long legal documents by extracting key information while removing unnecessary legal jargon.

A **privacy filter** will verify the summarized output to ensure compliance with:

- BNS Section 73 (victim identity protection)
- POCSO Act (child protection)
- Digital Personal Data Protection Act (DPDPA) 2023

This ensures that sensitive personal information is not disclosed before publication.

---

## Judicial Analytics (Proposed)

CourtInsight will also integrate **National Judicial Data Grid (NJDG)** data to provide interactive dashboards showing court performance metrics in a citizen-friendly format.

This feature will help users better understand the functioning and efficiency of the judicial system.

---

## Technology Stack

- Kotlin
- Jetpack Compose
- MVVM Architecture
- OkHttp
- Coroutines
- Hugging Face Models
- Retrieval-Augmented Generation (RAG)

---

## Disclaimer

This platform is intended for **educational and informational purposes only** and should not be considered legal advice.

---

## Team

Team CourtInsight

- Ashish Yadav
- Ayushi Yadav
- Vincent Silveira
