# EncodingChanger
A simple java swing application to change file encodings to UTF-8, especially developed for srt files.

A runnable gui for transforming selected files' encoding to UTF-8. 

This project provides many usefull swing examples including layouts, action listeners and executable jars for beginners.
It also contains a sample usage for **Mozilla's [juniversalchardet](https://mvnrepository.com/artifact/com.googlecode.juniversalchardet/juniversalchardet/1.0.3)-1.0.3 jar** to detect file encodings automatically.
In addition you can find beginner level file I/O samples with encoding transformations.

This is open for further development, please feel free to contribute.

Go Fun Yourself!

### TODO
* @todo DRAG AND DROP files
* @todo when no files are available deactivate convert all button
* @todo rearrange colors and refactor code (use a constants file etc.)  
* @todo add select all checkbox ability into center panel (for file selection)
* @todo add column titles and colum borders into center panel
* @todo error handling tests for exceptional cases, like when source encoding is not available
* @todo design and develop special character changing support in encodingChanger
* @todo when a file's encoding is changed, update its row in GUI 

# Dosya Encoding Dönüştürücü
Seçilen dosyaların encodinglerini UTF-8'e dönüştürmek için java swing ile geliştirilmiş grafikli uygulama.
*Çalıştırılabilen jar'i doğrudan indirerek kullanabilirsiniz.*
Özellikle SRT dosyalarını dönüştürmek için denenebilir.

Bu proje içerisinde Java Swing kod örnekleri (layoutlar, action listenerlar, vb.) hakkında örnek kodlar bulabilirsiniz.
Ayrıca dosya okuma/yazma işlemleri ve encoding dönüşümleri için önerkeler mevcuttur.
Mozilla juniversalchardet-1.0.3 jar ile otomatik olarak encoding tespiti yapılması örneğini bulabilirsiniz.

keyif amaçlı ve geliştirmeye açık bir kod havuzudur.

