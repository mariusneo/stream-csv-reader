Stream CSV Reader
=================

Really simple project used to read the contents of a file as a stream.
In the case of this proof of concept project, the file type is CSV.


The implementation of this proof of concept is adapting the method
java.io.BufferedReader#lines() 
in order to be able to provide a stream of custom objects, instead of 
a stream of String objects (the lines of the file).

Inspiration for this proof of concept came from the tutorial presented
on [mykong](https://www.mkyong.com/java8/java-8-stream-read-a-file-line-by-line/)'s page.


By rewriting the CSV record reader code in this fashion, the code looks much
more simplified:

```
try (Stream<Product> stream = new RecordsCSVReader<Product>("products.csv", PRODUCT_READER)
        .products()) {
    stream.forEach(System.out::println);
} catch (IOException e) {
    throw new RuntimeException("Exception occurred while retrieving the product records", e);
}
```