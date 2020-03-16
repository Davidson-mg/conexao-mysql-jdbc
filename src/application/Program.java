
package application; /*Classe do programa princiapal*/

import db.DB;
import db.DbException;
import db.DbIntegrityExcepetion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Program {

    public static void main(String[] args) {
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        Connection conn = null;
        
        Statement st = null;
        
        try{
            
            conn = DB.getConnection();
            
            
            /*inicio da transação*/
            
            conn.setAutoCommit(false); /*Não é para confirmar as operações automaticamente. Todas ficaram pendentes esperando uma confirmalçao
            explicita do progrador */
            
            st = conn.createStatement();
            
            
            int linha1 = st.executeUpdate("update seller set BaseSalary = 2000 where departmentid = 1");
            
            int x = 1; /*Estamos colocando esse if aqui apenas para simular um erro no meio de uma trnsação*/
            if (x < 2){
            
                throw new SQLException ("Erro fake");
                
            }
        
            int linha2 = st.executeUpdate("update seller set BaseSalary = 3090 where departmentid = 2");
            
            conn.commit(); /*Caso ocorro um erro do apartir do "conn.setAutoCommit(false)" e antes de chegar aqui no "conn.commit". Vai cair no "try" abaixo
            onde chamamos o rollback e fazendo com que retorna os registros anteriores do banco*/
            
            /*fim da transação*/
         
            System.out.println("Linha1: "+linha1+" linha2: "+linha2);
            
        }
        catch(SQLException e){
            
            try {
                conn.rollback();
                throw new DbException ("A transação foir retornada! Causado por: "+e.getMessage()); /*caso aconteça uma falha na transação*/
                
            } catch (SQLException ex) { /*Esse catch somos obrigados a colocar ao inserir o rollback. Caso aconteça um erro no rollback*/
                throw new DbException("Erro ao tentar voltar transação! Causado por: "+e.getMessage());
            }      
        }
    } 
}
