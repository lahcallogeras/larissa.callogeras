//
//  ContatosDAO.m
//  conex
//
//  Created by Gleison on 05/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import "ContatoDAO.h"

@implementation ContatoDAO

 
 // Inicializa o banco -------------------------------
 - (void)initDB {
     
     // Obtém os diretório de arquivos da aplicação
     NSArray *dirPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
     // Obtém o diretório para salvar o arquivo do banco
     NSString *docsDir = [dirPaths objectAtIndex:0];
     // Cria o caminho do arquivo do banco
     databasePath = [[NSString alloc] initWithString:[docsDir stringByAppendingPathComponent: @"conex.db"]];
 
     const char *dbpath = [databasePath UTF8String];
     // Inicializa o atributo contactDB com o banco
     if (sqlite3_open(dbpath, &conexDB) == SQLITE_OK){
         NSLog(@"ok");
     }
 }
 
 // Cria a tabela no banco ---------------------------
 - (void)createDatabase {
     // Chama o método acima...
     [self initDB];
     
     char *errMsg;
     const char *sql_dropCtt = "DROP TABLE IF EXISTS cadContatos;";
     
     // Cria a tabela no banco se ela não existir
     if (sqlite3_exec(conexDB, sql_dropCtt, NULL, NULL,&errMsg) == SQLITE_OK) {
         NSLog(@"Tabela Contatos apagada");
     } else {
         NSLog(@"Falha ao apagar tabela Contados");
     }

     
     
     const char *sql_stmt = "CREATE TABLE IF NOT EXISTS cadContatos (codigoCtt integer, codigoUsuLocalCtt integer not null, statusCtt text not null, codigoUsuCtt integer not null, usuarioUsuCtt text not null, nomeUsuCtt text not null, dataNascUsuCtt text not null, emailUsuCtt text not null, sexoUsuCtt text not null, dataCadUsuCtt text , nomeFotoCtt text);";
 
     // Cria a tabela no banco se ela não existir
     if (sqlite3_exec(conexDB, sql_stmt, NULL, NULL,&errMsg) == SQLITE_OK) {
            NSLog(@"Banco de Dados criado com sucesso");
    } else {
        NSLog(@"Falha ao criar Banco de Dados ");
    }
 
     sqlite3_close(conexDB);
 }
 

 
 
// Inserir um contato no banco ---------------------
- (void)insertContato:(Contato *)contato {
    [self initDB];

        char *sql = "INSERT INTO cadContatos (codigoCtt, codigoUsuLocalCtt, statusCtt, codigoUsuCtt, usuarioUsuCtt, nomeUsuCtt, dataNascUsuCtt, emailUsuCtt, sexoUsuCtt, dataCadUsuCtt, nomeFotoCtt) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    
        sqlite3_stmt *stmt;      
        if (sqlite3_prepare_v2(conexDB, sql, -1,&stmt, nil) == SQLITE_OK) {
            
            sqlite3_bind_int(stmt,  1, (contato.codigoCtt ));
            sqlite3_bind_int(stmt,  2, (contato.codigoUsuLocalCtt));
            sqlite3_bind_text(stmt, 3, [contato.statusCtt UTF8String], -1, NULL);
            sqlite3_bind_int(stmt,  4, (contato.codigoUsuCtt));
            sqlite3_bind_text(stmt, 5, [contato.usuarioUsuCtt UTF8String], -1, NULL);
            sqlite3_bind_text(stmt, 6, [contato.nomeUsuCtt UTF8String], -1, NULL);
            sqlite3_bind_text(stmt, 7, [contato.dataNascUsuCtt UTF8String], -1, NULL);
            sqlite3_bind_text(stmt, 8, [contato.emailUsuCtt UTF8String], -1, NULL);
            sqlite3_bind_text(stmt, 9, [contato.sexoUsuCtt UTF8String], -1, NULL);
            sqlite3_bind_text(stmt, 10,[contato.dataCadUsuCtt UTF8String], -1, NULL);
            sqlite3_bind_text(stmt, 11,[contato.nomeFotoCtt UTF8String], -1, NULL);
            
        }
        if (sqlite3_step(stmt) == SQLITE_DONE){
            NSLog(@"Contato adicionado");
        } else {
            NSLog(@"Falha ao adicionar contato");
        }
        
        sqlite3_finalize(stmt);
        sqlite3_close(conexDB);    
       
    
}

 /*
//Limpa Contatos
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
 
 //- (NSMutableArray *) getContatos: (int) idUsuario{
- (NSMutableArray *) getContatos: (int) codigoUsuario {
 
 
 [self initDB];
 
 NSMutableArray *contatosA= [[NSMutableArray alloc]init];
 
 sqlite3_stmt *statement;
     
 NSString *querySQL = [NSString stringWithFormat:@"SELECT *, (select count(*) FROM cadMensagens  where codigoUsuOriMsg = codigoUsuCtt and codigoUsuDestMsg = codigoUsuLocalCtt and statusLida = 'N')  FROM cadContatos where codigoUsuLocalCtt = ? order by nomeUsuCtt "];
 
 const char *query_stmt = [querySQL UTF8String];
 
 if (sqlite3_prepare_v2(conexDB, query_stmt, -1,&statement, NULL) == SQLITE_OK){
     
     sqlite3_bind_int(statement,  1, (codigoUsuario));
 
 while (sqlite3_step(statement) == SQLITE_ROW)
 {
     Contato *contatoCtt = [[Contato alloc]init]; 
     contatoCtt.codigoCtt =  sqlite3_column_int(statement, 0);
     contatoCtt.codigoUsuLocalCtt =  sqlite3_column_int(statement, 1);
     contatoCtt.statusCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 2)];
     contatoCtt.codigoUsuCtt =  sqlite3_column_int(statement, 3);
     contatoCtt.usuarioUsuCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 4)];
     contatoCtt.nomeUsuCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 5)];
     contatoCtt.dataNascUsuCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 6)];
     contatoCtt.emailUsuCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 7)];
     contatoCtt.sexoUsuCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 8)];
     contatoCtt.dataCadUsuCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 9)];
     contatoCtt.nomeFotoCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 10)];
     contatoCtt.qtdMsg =  sqlite3_column_int(statement, 11);
     [contatosA addObject:contatoCtt];
 
        }
 
    }
    sqlite3_finalize(statement);
    sqlite3_close(conexDB);
 
    return [NSMutableArray arrayWithArray:contatosA];
 }
 
 

- (Contato *) getContatoId:(int) usuContato {
    
    
    [self initDB];    
    sqlite3_stmt *statement;
   
    
    Contato *contatoCtt = [[Contato alloc]init];
    NSString *querySQL = [NSString stringWithFormat:@"SELECT * FROM cadContatos where codigoUsuCtt = ? "];
    const char *query_stmt = [querySQL UTF8String];
    
    
    if (sqlite3_prepare_v2(conexDB, query_stmt, -1,&statement, NULL) == SQLITE_OK){        
        sqlite3_bind_int(statement,  1, (usuContato));        
        while (sqlite3_step(statement) == SQLITE_ROW)
        {
            contatoCtt.codigoCtt =  sqlite3_column_int(statement, 0);
            contatoCtt.codigoUsuLocalCtt =  sqlite3_column_int(statement, 1);
            contatoCtt.statusCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 2)];
            contatoCtt.codigoUsuCtt =  sqlite3_column_int(statement, 3);
            contatoCtt.usuarioUsuCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 4)];
            contatoCtt.nomeUsuCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 5)];
            contatoCtt.dataNascUsuCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 6)];
            contatoCtt.emailUsuCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 7)];
            contatoCtt.sexoUsuCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 8)];
            contatoCtt.dataCadUsuCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 9)];
            contatoCtt.nomeFotoCtt = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 10)];
           
            
        }
        
    }
    
    sqlite3_finalize(statement);
    sqlite3_close(conexDB);
    
    return contatoCtt;
}

 
 
 @end
 
 
 
