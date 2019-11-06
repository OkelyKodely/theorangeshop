package org.o7planning.springmvcshoppingcart.model;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
 
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.o7planning.springmvcshoppingcart.dao.ProductDAO;
import org.o7planning.springmvcshoppingcart.entity.Product;
import org.o7planning.springmvcshoppingcart.model.PaginationResult;
import org.o7planning.springmvcshoppingcart.model.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
 
// Transactional for Hibernate
@Transactional
public class ProductDAOImpl implements ProductDAO {
 
    @Autowired
    private SessionFactory sessionFactory;
 
    public Product findProduct(String code) {

    	String hostName = "localhost:3320";
		String dbName = "mydb";
		String userName = "root";
		String passWord = "";

		Connection conn = null;
		Product product = new Product();
		ResultSet result = null;
		
		try {

    		try {
    		    Class.forName("com.mysql.jdbc.Driver");
    		    String url = "jdbc:mysql://" + hostName + "/" + dbName;
    		    conn = DriverManager.getConnection(url, userName, passWord);
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    		
    		result = conn.createStatement().executeQuery("select * from products where code = '"+code+"'");
    		if(result.next()) {
    			product.setCategory(result.getString("Category"));
    			System.out.println("category: "+product.getCategory());
    			product.setCode(code);
    			product.setCreateDate(result.getDate("Create_Date"));
    			product.setDescription(result.getString("description"));
    			product.setImage(result.getBytes("Image"));
    			product.setName(result.getString("name"));
    			product.setPrice(result.getDouble("price"));
    		}
    		conn.close();
    		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return product;
    }
 
    public ProductInfo findProductInfo(String code) {
        Product product = this.findProduct(code);
        
        if (product == null) {
            return null;
        }
        System.out.println(product.getName());
        System.out.println(product.getCategory());
        return new ProductInfo(product.getCode(), product.getName(), product.getCategory(), product.getPrice(), product.getDescription(), null);
    }
 
    public void save(ProductInfo productInfo) {
        String code = productInfo.getCode();
 
        Product product = null;
 
        boolean isNew = false;
        if (code != null) {
            product = this.findProduct(code);
        }
        if (product == null) {
            isNew = true;
            product = new Product();
            product.setCreateDate(new Date());
        }
        product.setCode(code);
        product.setName(productInfo.getName());
        product.setCategory(productInfo.getCategory());
        product.setPrice(productInfo.getPrice());
 
        if (productInfo.getFileData() != null) {
            byte[] image = productInfo.getFileData().getBytes();
            if (image != null && image.length > 0) {
                product.setImage(image);
            }
        }
        if (isNew) {
            this.sessionFactory.getCurrentSession().persist(product);
        }
        // If error in DB, Exceptions will be thrown out immediately
        // N?u có l?i t?i DB, ngo?i l? s? ném ra ngay l?p t?c
        this.sessionFactory.getCurrentSession().flush();
    }
 
    public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage,
            String likeName) {
        String sql = "Select new " + ProductInfo.class.getName() //
                + "(p.code, p.name, p.category, p.price, p.description, p.image) " + " from "//
                + Product.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        sql += " order by p.createDate desc ";
        //
        System.out.println(sql);
        Session session = sessionFactory.getCurrentSession();
 
        Query query = session.createQuery(sql);
        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
    }
 
    public PaginationResult<ProductInfo> queryProductsByCategory(int page, int maxResult, int maxNavigationPage,
            String likeName, String category) {
    	if(category == null)
    		return null;
    	
        String sql = "Select new " + ProductInfo.class.getName() //
                + "(p.code, p.name, p.category, p.price, p.description, p.image) " + " from "//
                + Product.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
//            sql += " Where p.category = '" + category + "' and lower(p.name) like :likeName ";
        }
        sql += " Where p.category = '" + category + "'";
        sql += " order by p.createDate desc ";
        //
        System.out.println(sql);
        Session session = sessionFactory.getCurrentSession();
 
        Query query = session.createQuery(sql);
        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
    }

    public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage) {
        return queryProducts(page, maxResult, maxNavigationPage, null);
    }
    
}