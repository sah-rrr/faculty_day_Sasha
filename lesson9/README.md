# Задания

## Задание 1. Поиск ошибок в логах
Найти все строки со словом ERROR во всех логах в каталоге logs (включая logs/old) и сохранить их в файл errors.txt в корне проекта.

```bash
grep -r "ERROR" logs > errors.txt
```

## Задание 2. Архивация старых логов
Создать каталог archived/ в корне проекта и переместить туда все файлы из logs/old.

```bash
mkdir -p archived/
mv logs/old/* archived/
```

## Задание 3. Подсчёт размера логов
Посчитать общий размер каталога logs и записать результат в logs_size.txt.

```bash
du -sh logs > logs_size.txt
```

## Задание 4. Нахождение самого большого лог-файла
Найти самый большой файл в каталоге logs (без учёта подкаталогов) и записать его имя в файл biglog.txt.

```bash
ls -S logs | head -n 1 > biglog.txt
```

## Задание 5. Подсчёт количества логов
Подсчитать количество файлов с расширением .log во всём каталоге logs и сохранить результат в log_count.txt.

```bash
find logs -type f -name "*.log" | wc -l > log_count.txt
```

## Задание 6. Поиск конфигурационных параметров
Найти во всех config/*.conf строки, содержащие слово "host", и записать в host_params.txt.

```bash
find config -type f -name "*.conf" | grep -r "host" config/*/conf > host_params.txt
```

## Задание 7. Создание резервного архива конфигов
Создать zip-архив config_backup.zip, содержащий все файлы из config/.

```bash
zip -r config_backup.zip config/
```

## Задание 8. Создание общего резервного архива
Создать zip-архив project_backup.zip, куда включить:
- все *.conf из config/
- все *.log из logs (включая old/)
- файл errors.txt (если он есть)

```bash
zip -r profect_backup.zip config/*.conf logs/*.log logs/old/*.log errors.txt
```

## Задание 9. Очистка пустых строк в логах
Создать файл cleaned_app.log, содержащий содержимое app.log без пустых строк.

```bash
grep -v "^$" app.log > cleaned_app.log
```

## Задание 10. Подсчёт количества строк в каждом конфиге
Создать файл conf_stats.txt с вида:
app.conf 12  
db.conf 8  
(где число — количество строк в файле)

```bash
wc -l config/*.conf | awk '{print $2, $1}' | head -n 1 > conf stats.txt
```


## Задача: Создать собственный архиватор
Напишите функцию, которая:

1. Принимает на вход **каталог с файлами** (например, `project_data/`) и путь к архиву (`archive.zip`).
2. Создает **ZIP-архив** всех файлов внутри каталога, сохраняя структуру подкаталогов.
3. Использует классы `FileInputStream`, `FileOutputStream` и `java.util.zip.ZipOutputStream`.
4. Обеспечивает корректное закрытие потоков и обработку исключений.
5. Добавить фильтр по расширению файлов, чтобы архивировать только `.txt` или `.log`.

### Требования:
- Не использовать сторонние библиотеки для архивирования (только стандартный API).
- Программа должна выводить в консоль список добавляемых файлов и их размер.


```bash
import java.io.*
import java.nio.file.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun createZipArchive(sourceDirPath: String, zipFilePath: String) {
    val sourceDir = File(sourceDirPath)
    if (!sourceDir.exists() || !sourceDir.isDirectory) {
        println("Ошибка: папка '$sourceDirPath' не существует/не является папкой")
        return
    }

    ZipOutputStream(FileOutputStream(zipFilePath)).use { zipOut ->
        sourceDir.walk().forEach { file ->
            if (file.isFile && (file.extension == "txt" || file.extension == "log")) {
                val relativePath = file.relativeTo(sourceDir).path
                val entry = ZipEntry(relativePath)
                zipOut.putNextEntry(entry)
                FileInputStream(file).use { input ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        zipOut.write(buffer, 0, bytesRead)
                    }
                }
                zipOut.closeEntry()
                println("Добавлен файл: $relativePath (${file.length()} байт)")
            }
        }
    }
    println("Архив создан: $zipFilePath")
}
```