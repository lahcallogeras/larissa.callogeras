//
//  MensagemDAO.h
//  conex
//
//  Created by Larii on 10/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "sqlite3.h"
#import "Mensagem.h"
#import "ContatoDAO.h"


@interface MensagemDAO : NSObject{
    sqlite3 *conexDB;
    NSString *databasePath;
    ContatoDAO *contatoDAO;
}



- (void)createDatabase;
- (void)insertMensagem:(Mensagem *) msg, int codUsuario;
- (void)deleteMensagem:(int) codContato, int codUsuario;
- (NSMutableArray *) getMensagem: (int) codigoUsu, int codigoCont;

@end
