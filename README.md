## 1. Nama Program

# **AeroCare: Sistem Informasi Pengelolaan Fasilitas dan Pemeliharaan Bandara**

## 2. Deskripsi Singkat

AeroCare adalah sistem informasi untuk membantu pengelolaan fasilitas dan pemeliharaan bandara. Sistem ini dirancang untuk mencatat data fasilitas, menerima laporan kerusakan, mengatur penugasan petugas, serta memantau progres penanganan.

AeroCare memiliki tiga jenis pengguna, yaitu **Admin**, **Supervisor**, dan **Petugas**. Setiap pengguna memiliki hak akses dan tanggung jawab yang berbeda. Sistem ini masih dalam tahap pengembangan; fitur dan dokumentasi akan diperbarui seiring dengan penyelesaian program.

## 3. Fitur Utama Program

Fitur yang dirancang dalam AeroCare meliputi:

- **Login dan hak akses pengguna** berdasarkan peran Admin, Supervisor, atau Petugas.
- **Pengelolaan data pengguna** oleh Admin.
- **Pengelolaan data fasilitas bandara**, termasuk penambahan, penampilan, perubahan, dan penghapusan data.
- **Pencatatan dan pengelolaan laporan kerusakan** fasilitas.
- **Pencarian laporan** berdasarkan kriteria seperti ID laporan, tanggal, fasilitas atau lokasi, status, prioritas, dan petugas.
- **Penugasan petugas** untuk menangani laporan kerusakan oleh Supervisor.
- **Pemantauan progres penanganan** dan riwayat progres oleh Supervisor.
- **Pencatatan progres pekerjaan** oleh Petugas.
- **Dashboard dan rekap laporan** untuk membantu pemantauan kondisi fasilitas serta penanganan kerusakan.

## 4. Tech Stack

| Bagian | Teknologi |
|---|---|
| Bahasa pemrograman | Java |
| Antarmuka pengguna | Java Swing |
| Basis data | MySQL |
| Koneksi Java ke basis data | JDBC |
| Perancangan diagram | Draw.io |

## 5. Perancangan Sistem

### 5.1 Flowchart

Flowchart berikut menggambarkan alur penggunaan dan proses utama AeroCare, mulai dari login hingga pengelolaan fasilitas, laporan, penugasan, progres, pencarian, dan rekap.

#### Mulai, Login, dan Hak Akses

<img width="1023" height="844" alt="Capstone_Flowchart-1  Mulai, Login   Hak Akses drawio" src="https://github.com/user-attachments/assets/586ed9cc-f5e3-46a1-b048-10093f00b0e1" />

#### Menu Admin

<img width="1563" height="414" alt="Capstone_Flowchart-2  Menu Admin drawio" src="https://github.com/user-attachments/assets/4bc45723-1ab4-47aa-a40b-82c68c9d5a50" />

#### Kelola Pengguna

<img width="2788" height="1198" alt="Capstone_Flowchart-3  Kelola Pengguna drawio" src="https://github.com/user-attachments/assets/1e020eb8-9b75-4a70-a924-b9db05559ea4" />


#### Kelola Fasilitas

<img width="2888" height="1108" alt="Capstone_Flowchart-4  Kelola Fasilitas drawio" src="https://github.com/user-attachments/assets/e88dcd94-d6d0-42fb-9d2f-e6626e9545f3" />


#### Pengelolaan Laporan Kerusakan

<img width="2398" height="1184" alt="Capstone_Flowchart-5  Laporan Kerusakan drawio" src="https://github.com/user-attachments/assets/4a1e83f0-b66f-42a7-978b-b69491ed80b2" />


#### Penugasan oleh Supervisor

<img width="1968" height="1694" alt="Capstone_Flowchart-6  Supervisor - Penugasan drawio" src="https://github.com/user-attachments/assets/47357cd1-c8f2-4253-8514-f56a90efeb2a" />


#### Pemantauan Progres oleh Supervisor

<img width="528" height="802" alt="Capstone_Flowchart-7  Supervisor - Pantau Progres drawio" src="https://github.com/user-attachments/assets/89c7c0cb-a940-4f81-ac33-fa7995824eea" />


#### Progres Penanganan oleh Petugas

<img width="3018" height="1578" alt="Capstone_Flowchart-8  Petugas - Progres Penanganan drawio" src="https://github.com/user-attachments/assets/69a2144c-db87-41ea-b7ed-5a3d55fbfd4f" />


#### Pencarian Laporan

<img width="851" height="1447" alt="Capstone_Flowchart-9  Pencarian  drawio" src="https://github.com/user-attachments/assets/4f18eeba-13c9-4a64-9d74-90384a99df4f" />


#### Dashboard dan Rekap

<img width="766" height="1212" alt="Capstone_Flowchart-10  Dashboard_Rekap drawio" src="https://github.com/user-attachments/assets/d54f2eac-4aac-4006-b899-01253cbb942a" />


### 5.2 Use Case Diagram

Use Case Diagram menunjukkan interaksi Admin, Supervisor, dan Petugas dengan fitur-fitur yang tersedia di AeroCare.

<img width="1414" height="1672" alt="Use Case AEROCARE drawio (10)" src="https://github.com/user-attachments/assets/13077903-cb3b-44d0-ac01-25e4f9cf42e1" />


### 5.3 Entity Relationship Diagram (ERD)

ERD menggambarkan entitas, atribut, serta hubungan antarentitas yang digunakan dalam sistem AeroCare. Perancangan ERD terdiri atas ERD Logical dan ERD Relational.

#### ERD Logical

ERD Logical menggambarkan entitas dan hubungan antarentitas secara konseptual dalam sistem AeroCare.

<img width="1600" height="764" alt="WhatsApp Image 2026-11-02 at 18 50 12" src="https://github.com/user-attachments/assets/6edf7616-b993-4a7f-8b88-7161399503b5" />


#### ERD Relational

ERD Relational menggambarkan struktur tabel, atribut, primary key, foreign key, serta hubungan antar tabel dalam basis data AeroCare.

<img width="1600" height="779" alt="WhatsApp Image 2026-11-02 at 18 50 12 (1)" src="https://github.com/user-attachments/assets/9511f91b-557a-42db-902c-a252291e517f" />

---

<!--
Bagian berikut akan ditambahkan setelah program selesai dikembangkan:
- Struktur Folder/Package
- Penerapan 4 Pilar OOP
- Cara Penggunaan Program dan screenshot GUI
- Kontributor
-->
