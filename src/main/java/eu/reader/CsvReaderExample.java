package eu.reader;

import com.csvreader.CsvReader;
import eu.reader.model.Product;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.Stream;

public class CsvReaderExample {

    private static Function<CsvReader, Product> PRODUCT_READER = (CsvReader products) -> {
        try {
            String productID = products.get("ProductID");
            String productName = products.get("ProductName");
            String supplierID = products.get("SupplierID");
            String categoryID = products.get("CategoryID");
            String quantityPerUnit = products.get("QuantityPerUnit");
            String unitPrice = products.get("UnitPrice");
            String unitsInStock = products.get("UnitsInStock");
            String unitsOnOrder = products.get("UnitsOnOrder");
            String reorderLevel = products.get("ReorderLevel");
            String discontinued = products.get("Discontinued");

            Product product = new Product();
            product.setId(productID);
            product.setName(productName);
            product.setSupplierId(supplierID);
            product.setCategoryId(categoryID);
            product.setQuantityPerUnit(quantityPerUnit);
            product.setUnitPrice(new BigDecimal(unitPrice));
            product.setUnitsInStock(Integer.parseInt(unitsInStock));
            product.setUnitsInOrder(Integer.parseInt(unitsOnOrder));
            product.setReorderLevel(Integer.parseInt(reorderLevel));
            product.setDiscounted(Boolean.parseBoolean(discontinued));
            return product;

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    };


    public static void main(String[] args) {
        try (Stream<Product> stream = new RecordsCSVReader<Product>("products.csv", PRODUCT_READER)
                .products()) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while retrieving the product records", e);
        }


    }


}