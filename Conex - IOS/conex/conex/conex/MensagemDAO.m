//
//  MensagemDAO.m
//  conex
//
//  Created by Larii on 10/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import "MensagemDAO.h"
#import "ContatoDAO.h"

@implementation MensagemDAO




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
    const char *sql_stmt = "CREATE TABLE IF NOT EXISTS cadMensagens (codigoMsg integer , codigoUsuOriMsg integer ,  codigoUsuDestMsg integer , dataMsg text, textoMsg text not null, statusLida text not null);";
    
    // Cria a tabela no banco se ela não existir
    if (sqlite3_exec(conexDB, sql_stmt, NULL, NULL,
                     &errMsg) == SQLITE_OK) {
        NSLog(@"Banco de Dados criado com sucesso");
    } else {
        NSLog(@"Falha ao criar Banco de Dados ");
    }
    
    sqlite3_close(conexDB);
}

// Inserir um msg no banco ---------------------
- (void)insertMensagem:(Mensagem *) msg, int codUsuario {
    [self initDB];
    
    @try {
        
        char *sql = "INSERT INTO cadMensagens (codigoMsg, codigoUsuOriMsg, codigoUsuDestMsg, dataMsg, textoMsg, statusLida) VALUES (?,?,?,?,?,?)";
                      
        
        sqlite3_stmt *stmt;
        sqlite3_close(conexDB);
        [self initDB];
        if (sqlite3_prepare_v2(conexDB, sql, -1,&stmt, nil) == SQLITE_OK) {
            
            sqlite3_bind_int(stmt,  1, (msg.codigoMsg));
            
            if(msg.usuDestino != nil){
                sqlite3_bind_int(stmt,  2, (codUsuario));
                sqlite3_bind_int(stmt,  3, (msg.usuDestino.codigoUsuCtt));
            }
            else{
                
                sqlite3_bind_int(stmt,  2,  (msg.usuOrig.codigoUsuCtt));
                sqlite3_bind_int(stmt,  3,  (codUsuario)); 
            
            }
            
            sqlite3_bind_text(stmt, 4, [msg.dataMsg UTF8String], -1, NULL);
            sqlite3_bind_text(stmt, 5,[msg.textoMsg UTF8String], -1, NULL);
            sqlite3_bind_text(stmt, 6,[msg.statusLida UTF8String], -1, NULL);
            
        }
        if (sqlite3_step(stmt) == SQLITE_DONE){
            NSLog(@"Msg adicionado");
        } else {
            NSLog(@"Falha ao adicionar Msg");
        }
        
        sqlite3_finalize(stmt);
        sqlite3_close(conexDB);
    }
    @catch (NSException *e) {
        NSLog(@"Exception: %@Msg", e);
    }
    
    
}


- (NSMutableArray *) getMensagem: (int) codigoUsu, int codigoCont {
    
    
    [self initDB];    
    NSMutableArray *msgA= [[NSMutableArray alloc]init];    
    sqlite3_stmt *statement;
    
    
    contatoDAO = [[ContatoDAO alloc] init];
    
    char *sql = "UPDATE cadMensagens SET statusLida = ? WHERE (codigoUsuOriMsg = ? OR codigoUsuOriMsg = ?) AND (codigoUsuDestMsg = ? OR codigoUsuDestMsg = ?)";
    
    sqlite3_stmt *stmtUpdate;
    if (sqlite3_prepare_v2(conexDB, sql, -1, &stmtUpdate, nil) == SQLITE_OK) {
        sqlite3_bind_text(stmtUpdate, 1,[@"S" UTF8String], -1, NULL);        
        sqlite3_bind_int(stmtUpdate,  2, (codigoUsu));
        sqlite3_bind_int(stmtUpdate,  3, (codigoCont));
        sqlite3_bind_int(stmtUpdate,  4, (codigoUsu));
        sqlite3_bind_int(stmtUpdate,  5, (codigoCont));
        
    }
    
    if (sqlite3_step(stmtUpdate) == SQLITE_DONE){
        NSLog(@"Record updated msg");
    } else {
        NSLog(@"Failed to update msg");
    }
    sqlite3_finalize(stmtUpdate);
    
    
    NSString *querySQL = [NSString stringWithFormat:@" Select * from (select * from cadMensagens where codigoUsuOriMsg = ?	 and  codigoUsuDestMsg = ? union  select * from cadMensagens   where codigoUsuOriMsg = ?  and  codigoUsuDestMsg = ?  ) "];
    
    const char *query_stmt = [querySQL UTF8String];
    
    if (sqlite3_prepare_v2(conexDB, query_stmt, -1,&statement, NULL) == SQLITE_OK){
        
        
        sqlite3_bind_int(statement,  1, (codigoUsu));        
        sqlite3_bind_int(statement,  2, (codigoCont));
        sqlite3_bind_int(statement,  3, (codigoCont));
        sqlite3_bind_int(statement,  4, (codigoUsu));
        
        while (sqlite3_step(statement) == SQLITE_ROW)
        {
            Mensagem *msg = [[Mensagem alloc]init];
            
            msg.codigoMsg =  sqlite3_column_int(statement, 0);
            
            if(sqlite3_column_int(statement, 1) != codigoUsu)
            {
                msg.usuOrig = [contatoDAO getContatoId:sqlite3_column_int(statement, 1)];//codigoUsuOriMsg
            }
            else{
                msg.usuDestino = [contatoDAO getContatoId:sqlite3_column_int(statement, 2)];//codigoUsuDestMsg
            }
                     
            msg.dataMsg = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 3)];
            msg.textoMsg = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 4)];
            msg.statusLida = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 5)];
            
            [msgA addObject:msg];
            
        }
        
        sqlite3_finalize(statement);
    }
    
    
    sqlite3_close(conexDB);    
    return [NSMutableArray arrayWithArray:msgA];
}


// Inserir um msg no banco ---------------------
- (void)deleteMensagem:(int) codContato, int codUsuario {
    [self initDB];
    
    @try {
        
        char *sql = "DELETE FROM cadMensagens WHERE (codigoUsuOriMsg = ? OR codigoUsuOriMsg = ?) AND (codigoUsuDestMsg = ? OR codigoUsuDestMsg = ?) ";
        
        
        sqlite3_stmt *stmt;
        sqlite3_close(conexDB);
        [self initDB];
        if (sqlite3_prepare_v2(conexDB, sql, -1,&stmt, nil) == SQLITE_OK) {
            
            sqlite3_bind_int(stmt,  1, (codUsuario));
            sqlite3_bind_int(stmt,  2, (codContato));
            sqlite3_bind_int(stmt,  3, (codUsuario));
            sqlite3_bind_int(stmt,  4, (codContato));
            
        }
        if (sqlite3_step(stmt) == SQLITE_DONE){
            NSLog(@"Msg apagadas");
        } else {
            NSLog(@"Falha ao apagar Msg");
        }
        
        sqlite3_finalize(stmt);
        sqlite3_close(conexDB);
    }
    @catch (NSException *e) {
        NSLog(@"Exception: %@Msg", e);
    }
    
    
}



@end
