//
//  UsuarioDAO.m
//  conex
//
//  Created by Gleison on 02/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import "UsuarioDAO.h"

@implementation UsuarioDAO

// Inicializa o banco -------------------------------
- (void)initDB {
    // Obtém os diretório de arquivos da aplicação
    NSArray *dirPaths =
    NSSearchPathForDirectoriesInDomains(
                                        NSDocumentDirectory, NSUserDomainMask, YES);
    // Obtém o diretório para salvar o arquivo do banco
    NSString *docsDir = [dirPaths objectAtIndex:0];
    // Cria o caminho do arquivo do banco
    databasePath = [[NSString alloc] initWithString:
                    [docsDir stringByAppendingPathComponent:
                     @"conex.db"]];
    
    const char *dbpath = [databasePath UTF8String];
    // Inicializa o atributo contactDB com o banco
    if (sqlite3_open(dbpath, &conexDB) == SQLITE_OK){
        NSLog(@"Failed to open/create database - 1");
    }
}

// Cria a tabela no banco ---------------------------
- (void)createDatabase {
    // Chama o método acima...
    [self initDB];
    
    char *errMsg;
    const char *sql_stmt = "CREATE TABLE IF NOT EXISTS cadUsuario (codigoUsu integer , usuarioUsu text not null, senhaUsu text not null, nomeUsu text not null, dataNascUsu text, codigoPaisUsu integer not null, emailUsu text not null, sexoUsu text not null, manterConUsu text , dataCadUsu text , dataLoginUsu text , nomeFotoUsu text);";
    
    // Cria a tabela no banco se ela não existir
    if (sqlite3_exec(conexDB, sql_stmt, NULL, NULL,
                     &errMsg) == SQLITE_OK) {
        NSLog(@"Banco de Dados criado com sucesso");
    } else {
        NSLog(@"Falha ao criar Banco de Dados ");
    }
    
    sqlite3_close(conexDB);
}



-(Usuario*) autenticaUsu:(NSString*) usuarioUsu: (NSString*)senhaUsu{
    
    
    
    Usuario *usuarioUsuVAIIIII = [[Usuario alloc] init];
    [self initDB];
    
    
    NSString *querySQL = [NSString stringWithFormat:@"select * from cadUsuario where usuarioUsu  = ? AND senhaUsu = ?"];
    const char *query_stmt = [querySQL UTF8String];
    sqlite3_stmt *statement;
    
    
    @try {
        
        
        if(sqlite3_prepare_v2(conexDB, query_stmt , -1, &statement, NULL)== SQLITE_OK){
            
            sqlite3_bind_text(statement, 1, [usuarioUsu UTF8String], -1, NULL);
            sqlite3_bind_text(statement, 2, [senhaUsu UTF8String], -1, NULL);
            
            
            while(sqlite3_step(statement) == SQLITE_ROW)
            {
                int codUsu = sqlite3_column_int(statement, 0);
                NSString * nomUsu = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 3)];
                
                
                usuarioUsuVAIIIII.codigoUsu = codUsu;
                usuarioUsuVAIIIII.usuarioUsu = nomUsu;
                
                //  NSArray *teste = [[NSArray alloc]init];;
                return usuarioUsuVAIIIII;
            }
            
            return usuarioUsuVAIIIII;
            
        };
        
        
    }
    @catch (NSException *exception) {
    }
    @finally {
        sqlite3_finalize(statement);
        sqlite3_close(conexDB);
        
        
    }
}

-(Usuario*) conectaUsu{
    
    
    
    Usuario *usuario= [[Usuario alloc] init];
    [self initDB];
    
    
    NSString *querySQL = [NSString stringWithFormat:@"select * from cadUsuario where manterConUsu  = 'YES'"];
    const char *query_stmt = [querySQL UTF8String];
    sqlite3_stmt *statement;
    
    
    @try {
        
        
        if(sqlite3_prepare_v2(conexDB, query_stmt , -1, &statement, NULL)== SQLITE_OK){
            
            
            while(sqlite3_step(statement) == SQLITE_ROW)
            {
                usuario.codigoUsu = sqlite3_column_int(statement, 0);
                usuario.usuarioUsu= [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 1)];
                usuario.senhaUsu= [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 2)];
                usuario.nomeUsu = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 3)];
                usuario.manterConUsu = @"YES";
                
                
            }
            
            
            
        }
        return usuario;
        
    }
    @catch (NSException *exception) {
    }
    @finally {
        sqlite3_finalize(statement);
        sqlite3_close(conexDB);
        
        
        
    }
}






-(void)limpaManterConectado{
    
    [self initDB];
    sqlite3_stmt *sqlStmt;
    @try {
        
        char *sql = "UPDATE cadUsuario SET manterConUsu = 'NO'";
        

        sqlite3_prepare_v2(conexDB, sql, -1, &sqlStmt, nil);
        if (sqlite3_step(sqlStmt) == SQLITE_DONE){
            NSLog(@" manterConUsu updated");
        } else {
            NSLog(@"Failed to update manterConUsu");
        }

        
    }
    @catch (NSException *exception) {
        
    }
    @finally {
        sqlite3_finalize(sqlStmt);
        sqlite3_close(conexDB);
        
    }
    

}



// Inserir um contato no banco ---------------------
- (void)insertUser:(Usuario *)usuario { //inicio
    
    
    [self limpaManterConectado];
    
    [self initDB];
    
    
    char* sqlCount= " select count(*) from cadUsuario where codigoUsu  = ?";
    sqlite3_stmt *selectStatement;
    
    
    
    if(sqlite3_prepare_v2(conexDB, sqlCount , -1, &selectStatement, NULL)== SQLITE_OK){ //if 01
        
        sqlite3_bind_int(selectStatement,  1, (usuario.codigoUsu ));
        
        
            if(sqlite3_step(selectStatement) == SQLITE_ROW) // if 02
            {
                
                int numrows= sqlite3_column_int(selectStatement, 0);
                if (numrows == 0){   //if 03
                    
                    sqlite3_finalize(selectStatement);
                    char *sql = "INSERT INTO cadUsuario (codigoUsu, usuarioUsu, senhaUsu, nomeUsu, dataNascUsu, codigoPaisUsu, emailUsu, sexoUsu, manterConUsu, dataCadUsu, dataLoginUsu,nomeFotoUsu) VALUES (?,?,?,?,?, ?,?,?,?,?,?,?)";
                    sqlite3_stmt *stmt;
                    if (sqlite3_prepare_v2(conexDB, sql, -1,&stmt, nil) == SQLITE_OK) {  // if 04
                        
                        sqlite3_bind_int(stmt,  1, (usuario.codigoUsu ));
                        sqlite3_bind_text(stmt, 2, [usuario.usuarioUsu UTF8String], -1, NULL);
                        sqlite3_bind_text(stmt, 3, [usuario.senhaUsu UTF8String], -1, NULL);
                        sqlite3_bind_text(stmt, 4, [usuario.nomeUsu UTF8String], -1, NULL);
                        sqlite3_bind_text(stmt, 5, [usuario.dataNascUsu UTF8String], -1, NULL);
                        sqlite3_bind_int (stmt, 6, (usuario.codigoPaisUsu ));
                        sqlite3_bind_text(stmt, 7, [usuario.emailUsu UTF8String], -1, NULL);
                        sqlite3_bind_text(stmt, 8, [usuario.sexoUsu UTF8String], -1, NULL);
                        sqlite3_bind_text(stmt, 9, [usuario.manterConUsu UTF8String], -1, NULL);
                        sqlite3_bind_text(stmt, 10, [usuario.dataCadUsu UTF8String], -1, NULL);
                        sqlite3_bind_text(stmt, 11, [usuario.dataLoginUsu UTF8String], -1, NULL);
                        sqlite3_bind_text(stmt, 12, [usuario.fotoUsu UTF8String], -1, NULL);
                    }   //fim if 04
                    if (sqlite3_step(stmt) == SQLITE_DONE){  //if 05
                        NSLog(@"Usuario adicionado"); 
                    } // fim if 05
                    else {
                        NSLog(@"Falha ao adicionar usuario");
                    }
                    
                    
                    
                    
                    sqlite3_finalize(stmt);
                    sqlite3_close(conexDB);
                    
                    
                    
                } // fim if 03
                
                else{ //else
                    sqlite3_finalize(selectStatement);
                    char *sql = "UPDATE cadUsuario SET nomeUsu = ?, senhaUsu = ?,manterConUsu=?,  emailUsu = ?, nomeFotoUsu = ? WHERE codigoUsu = ? ";
                    
                    sqlite3_stmt *stmt;
                    if (sqlite3_prepare_v2(conexDB, sql, -1, &stmt, nil) == SQLITE_OK) {
                        sqlite3_bind_text(stmt, 1,[usuario.nomeUsu UTF8String], -1, NULL);
                        sqlite3_bind_text(stmt, 2,[usuario.senhaUsu UTF8String], -1, NULL);
                        sqlite3_bind_text(stmt, 3,[usuario.manterConUsu UTF8String], -1, NULL);
                        sqlite3_bind_text(stmt, 4,[usuario.emailUsu UTF8String], -1, NULL);
                        sqlite3_bind_text(stmt, 5, [usuario.fotoUsu UTF8String], -1, NULL);
                        sqlite3_bind_int(stmt, 6, (usuario.codigoUsu ));
   
                    }
                    
                    if (sqlite3_step(stmt) == SQLITE_DONE){
                        NSLog(@"Record updated");
                    } else {
                        NSLog(@"Failed to update contact");
                    }
                   sqlite3_finalize(stmt);
                   sqlite3_close(conexDB);
                    
                
                } //fim else
                
                
                
                
            }// fim if 02
        
        
        }// fim if 01
        
        
    } // fim








/*
// Atualizar contato no banco ----------------------
- (void)updateContact:(NGContact *)contact {
    [self initDB];
    
    char *sql = "UPDATE CONTACTS SET NAME = ?,
    ADDRESS = ?, PHONE = ? WHERE ID = ?;";
    
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(contactDB, sql, -1,
                           &stmt, nil) == SQLITE_OK) {
        sqlite3_bind_text(stmt, 1,
                          [contact.name UTF8String], -1, NULL);
        sqlite3_bind_text(stmt, 2,
                          [contact.address UTF8String], -1, NULL);
        sqlite3_bind_text(stmt, 3,
                          [contact.phone UTF8String], -1, NULL);
        sqlite3_bind_int(stmt, 4, contact._id);
    }
    
    if (sqlite3_step(stmt) == SQLITE_DONE){
        NSLog(@"Record updated");
    } else {
        NSLog(@"Failed to update contact");
    }
    sqlite3_finalize(stmt);
    sqlite3_close(contactDB);
}

// Excluir contato ---------------------------------
- (void)deleteContact:(NGContact *)contact {
    [self initDB];
    
    char *sql = "DELETE FROM CONTACTS WHERE ID = ?;";
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(contactDB, sql, -1,
                           &stmt, nil) == SQLITE_OK) {
        
        sqlite3_bind_int(stmt, 1, contact._id);
    }
    if (sqlite3_step(stmt) == SQLITE_DONE){
        NSLog(@"Record removed");
    } else {
        NSLog(@"Failed to removed contact");
    }
    sqlite3_finalize(stmt);
    sqlite3_close(contactDB);
}
*/
// Obter lista de objetos do banco -----------------
- (NSArray *) getUser{
    [self initDB];
    
    NSMutableArray *usuarioA =
    [[NSMutableArray alloc]init];
    
    sqlite3_stmt *statement;
    
    NSString *querySQL = [NSString stringWithFormat:
                          @"SELECT * FROM cadUsuario "];
    
    const char *query_stmt = [querySQL UTF8String];
    
    if (sqlite3_prepare_v2(conexDB, query_stmt, -1,
                           &statement, NULL) == SQLITE_OK){
        
        while (sqlite3_step(statement) == SQLITE_ROW)
        {
            NSInteger codigoUsu =  sqlite3_column_int(statement, 0);
            
            NSString *usuarioUsu = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 1)];
            
/*NSString *addressField = [[NSString alloc]
                                      initWithUTF8String:(const char *)
                                      sqlite3_column_text(statement, 2)];
            
            NSString *phoneField = [[NSString alloc]   
                                    initWithUTF8String:(const char *)   
                                    sqlite3_column_text(statement, 3)];  
            */
            Usuario *usuario= [[Usuario alloc]init];
            usuario.codigoUsu = codigoUsu;
            usuario.usuarioUsu = usuarioUsu;
           
            
            [usuarioA addObject:usuario];
        }  
        sqlite3_finalize(statement);  
    }  
    sqlite3_close(conexDB);
    
    return [NSArray arrayWithArray:usuarioA];
}  





@end

