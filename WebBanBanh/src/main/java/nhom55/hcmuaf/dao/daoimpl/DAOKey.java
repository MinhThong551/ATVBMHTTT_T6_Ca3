package nhom55.hcmuaf.dao.daoimpl;

import nhom55.hcmuaf.beans.Key;
import nhom55.hcmuaf.beans.Role;
import nhom55.hcmuaf.database.JDBIConnector;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import nhom55.hcmuaf.security.DSA;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class DAOKey {
    public static List<Key> keyList(){
        String query="select id,idAccount,userName,`key`,dayReceive,dayExpired,dayCanceled,CASE WHEN (NOW() BETWEEN dayReceive AND dayExpired) and (dayCanceled is null) THEN 1 ELSE 0 END AS status  from user_keys";
        Jdbi me = JDBIConnector.me();
        return me.withHandle(handle -> {
          return  handle.createQuery(query).mapToBean(Key.class).stream().toList();
        });

    }
    public static List<Key> accountKeyList(int idAccount){
        String query="select id,`key`,dayReceive,dayExpired,dayCanceled,CASE WHEN (NOW() BETWEEN dayReceive AND dayExpired) and (dayCanceled is null)  THEN 1 ELSE 0 END AS status from user_keys where idAccount =:idAccount order by dayReceive desc";
        Jdbi me = JDBIConnector.me();
        return me.withHandle(handle -> {
            return  handle.createQuery(query).bind("idAccount",idAccount).mapToBean(Key.class).stream().toList();
        });
    }
    public static Key accountKeyNow(int idAccount){
        String query="SELECT \n" +
                "    id,\n" +
                "    `key`,\n" +
                "    dayReceive,\n" +
                "    dayExpired,\n" +
                "    dayCanceled,\n" +
                "    CASE WHEN (NOW() BETWEEN dayReceive AND dayExpired) and (dayCanceled is null) THEN 1 ELSE 0 END AS isBetween\n" +
                "FROM user_keys\n" +
                "WHERE idAccount = :idAccount\n and dayCanceled is null " +
                "ORDER BY dayReceive DESC\n" +
                "LIMIT 1";
        Jdbi me = JDBIConnector.me();
        return me.withHandle(handle -> {
            return  handle.createQuery(query).bind("idAccount",idAccount).mapToBean(Key.class).findFirst().orElse(null);
        });
    }
    public static Key addKey(int idAccount,String userName,String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        DSA.verifyPublicKey(publicKey);
        boolean isEnable;
        Jdbi me = JDBIConnector.me();
        isEnable = DAOKey.isExistPublicKey(publicKey);
        final String[] insertedKey = {""};
        if(isEnable){
            me.useHandle(handle -> {
                handle.begin();
                try {
                    String query;
                    disableAllOldKey(idAccount,handle);
                    query = "INSERT INTO `user_keys` (`idAccount`, `userName`, `key`, `dayExpired`) VALUES (:idAccount,:userName,:key,DATE_ADD(NOW(), INTERVAL 70 DAY))";
                    insertedKey[0] =handle.createUpdate(query).bind("idAccount",idAccount).bind("userName",userName).bind("key",publicKey)
                            .executeAndReturnGeneratedKeys("id").mapTo(String.class).findFirst().orElse("-1");
                    handle.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    handle.rollback();

                }
            });

            return findKeyById(insertedKey[0]);
        }else{
            return null;
        }

    }
    public static Key importKey(int idAccount,String userName,String publicKey,String dayExpired) throws NoSuchAlgorithmException, InvalidKeySpecException {
        DSA.verifyPublicKey(publicKey);
        boolean isEnable;
        Jdbi me = JDBIConnector.me();

            isEnable = DAOKey.isExistPublicKey(publicKey);
        final String[] insertedKey = {""};
           if(isEnable){
               me.useHandle(handle -> {
                   handle.begin();
                   try {
                       String query;
                       disableAllOldKey(idAccount,handle);
                       query = "INSERT INTO `user_keys` (`idAccount`, `userName`, `key`, `dayExpired`) VALUES (:idAccount,:userName,:key, :dayExpired)";
                       insertedKey[0] = handle.createUpdate(query).bind("idAccount",idAccount).bind("userName",userName).bind("key",publicKey)
                               .bind("dayExpired",dayExpired).executeAndReturnGeneratedKeys("id").mapTo(String.class).one();
                       handle.commit();
                   } catch (Exception e) {
                       e.printStackTrace();
                       handle.rollback();

                   }
               });
               return findKeyById(insertedKey[0]);
           }else{
               return null;
           }

    }
    public static Key findKeyById(String keyId){
        Jdbi me = JDBIConnector.me();
        String query = "select id,`key`,dayReceive,dayExpired,dayCanceled,CASE WHEN (NOW() BETWEEN dayReceive AND dayExpired) and (dayCanceled is null) THEN 1 ELSE 0 END AS status from user_keys where id = :id";
        return me.withHandle(handle->handle.createQuery(query).bind("id",keyId).mapToBean(Key.class).findFirst().orElse(null));
    }
    public static boolean isExistPublicKey(String publicKey) {
        Jdbi me = JDBIConnector.me();
        String query="SELECT EXISTS(\n" +
                "select id\n" +
                "from user_keys\n" +
                "where `key` = :publicKey\n" +
                ")";
        return me.withHandle(handle -> handle.createQuery(query).bind("publicKey",publicKey).mapTo(Integer.class).first()==0);
    }

    public static boolean disableAllOldKey(int idAccount, Handle handle){
        String query = "UPDATE `user_keys` SET dayCanceled=now(),dayExpired=now() WHERE (`idAccount` = :idAccount and dayCanceled is null);";;
        if(handle!=null){
            return handle.createUpdate(query).bind("idAccount",idAccount).execute()>0;
        }else{
            Jdbi me = JDBIConnector.me();
            return me.withHandle(replaceHandle -> replaceHandle.createUpdate(query).bind("idAccount",idAccount).execute()>0);
        }
    }

    public static void main(String[] args) {
        accountKeyList(1);
    }

    public static Key latestKey(int idAccount) {
        String query = "select id,`key`,dayReceive,dayExpired,dayCanceled from user_keys where idAccount =:idAccount order by dayCanceled desc limit 1\n";
        Jdbi me = JDBIConnector.me();
        return me.withHandle(handle -> handle.createQuery(query).bind("idAccount",idAccount).mapToBean(Key.class).first());
    }
}
