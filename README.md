# Періодичні видання (XML)

Лабораторна робота №2 з ООП, варіант 11 «Періодичні видання».

## Що робить програма

1. Перевіряє `papers.xml` на відповідність схемі `papers.xsd`.
2. Розбирає документ трьома парсерами — DOM, SAX і StAX — і перевіряє, що результат однаковий.
3. Сортує видання за допомогою `Comparator`: за назвою, за обсягом, за типом і назвою.
4. За допомогою `papers-by-type.xsl` перетворює документ у новий XML, де видання згруповані за типом
   (`newspaper`, `magazine`, `booklet`).

Усі повідомлення виводяться через Log4j2: у консоль (рівень INFO) і у файл `logs/periodicals.log` (рівень DEBUG).

## Файли

| Файл | Призначення |
|---|---|
| `src/main/resources/papers.xsd` | схема: прості й комплексні типи, переліки, межі значень, атрибути, тип ID |
| `src/main/resources/papers.xml` | вхідний документ |
| `src/main/resources/papers-by-type.xsl` | перетворення з групуванням за типом |
| `src/main/resources/log4j2.xml` | налаштування логування |

## Пакети

| Пакет | Призначення |
|---|---|
| `ua.knu.periodicals.model` | класи, що відповідають схемі: `Paper`, `Chars`, `PublicationType`, `Language` |
| `ua.knu.periodicals.parser` | парсери DOM, SAX, StAX і спільний `PaperBuilder` |
| `ua.knu.periodicals.validation` | валідація XML за XSD |
| `ua.knu.periodicals.transform` | XSL-перетворення |
| `ua.knu.periodicals.sorting` | компаратори |
| `ua.knu.periodicals.app` | точка входу |

## Запуск

Потрібні JDK 17+ та Maven.

```
mvn test
```

В IntelliJ IDEA відкрийте `pom.xml` як проєкт і запустіть `PeriodicalsApplication`.
Результат перетворення записується у `papers-by-type.xml` у робочій директорії
(інший шлях можна передати першим аргументом).
