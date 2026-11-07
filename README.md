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

| Proses | Diagram |
|---|---|
| Mulai, Login, dan Hak Akses | <img width="1023" height="844" alt="Capstone_Flowchart-1  Mulai, Login   Hak Akses drawio" src="https://github.com/user-attachments/assets/53713785-3bfb-40be-ae8a-a3cdbf1f058e" />
 |
| Menu Admin | <img width="1563" height="414" alt="Capstone_Flowchart-2  Menu Admin drawio" src="https://github.com/user-attachments/assets/a929d556-5566-4acf-8f3c-3f97e51cc147" />
 |
| Kelola Pengguna | <img width="2788" height="1198" alt="Capstone_Flowchart-3  Kelola Pengguna drawio" src="https://github.com/user-attachments/assets/75861ca7-fadf-45e7-bfce-25c80b66f84b" />
 |
| Kelola Fasilitas | <img width="2888" height="1108" alt="Capstone_Flowchart-4  Kelola Fasilitas drawio" src="https://github.com/user-attachments/assets/96311865-f026-4658-991b-9542c2746af1" />
 |
| Pengelolaan Laporan Kerusakan | ![Flowchart Laporan Kerusakan](PATH_FLOWCHART_LAPORAN_KERUSAKAN) |
| Penugasan oleh Supervisor | ![Flowchart Penugasan Supervisor](PATH_FLOWCHART_PENUGASAN_SUPERVISOR) |
| Pemantauan Progres oleh Supervisor | ![Flowchart Pantau Progres](PATH_FLOWCHART_PANTAU_PROGRES) |
| Progres Penanganan oleh Petugas | ![Flowchart Progres Petugas](PATH_FLOWCHART_PROGRES_PETUGAS) |
| Pencarian Laporan | ![Flowchart Pencarian Laporan](PATH_FLOWCHART_PENCARIAN_LAPORAN) |
| Dashboard dan Rekap | ![Flowchart Dashboard dan Rekap](PATH_FLOWCHART_DASHBOARD_REKAP) |

### 5.2 Use Case Diagram

Use Case Diagram menunjukkan interaksi Admin, Supervisor, dan Petugas dengan fitur-fitur yang tersedia di AeroCare.

![Use Case Diagram AeroCare](PATH_USE_CASE_DIAGRAM)

### 5.3 Entity Relationship Diagram (ERD)

ERD menggambarkan entitas, atribut, serta hubungan data yang digunakan dalam sistem AeroCare.

> Ganti placeholder berikut dengan lokasi gambar ERD setelah berkas diagram tersedia di repositori.

| Jenis ERD | Diagram |
|---|---|
| ERD Logical | ![ERD Logical AeroCare](PATH_ERD_LOGICAL) |
| ERD Relational | ![ERD Relational AeroCare](PATH_ERD_RELATIONAL) |

---

<!--
Bagian berikut akan ditambahkan setelah program selesai dikembangkan:
- Struktur Folder/Package
- Penerapan 4 Pilar OOP
- Cara Penggunaan Program dan screenshot GUI
- Kontributor
-->
