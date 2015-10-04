//
//  ContatoDAO.h
//  conex
//
//  Created by Larii on 05/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "sqlite3.h"
#import "Contato.h"


@interface ContatoDAO : NSObject{
    sqlite3 *conexDB;
    NSString *databasePath;
    UIImage *fotoCtt;
}

@property (nonatomic, retain) UIImage *fotoCtt;
- (void)createDatabase;
- (void)insertContato:(Contato *)contato;
//- (void)updateUser:(Usuario *)usuaroio;
//- (void)deleteUser:(Usuario *)usuario;
-  (NSMutableArray *) getContatos: (int) codigoUsuario;
- (Contato *) getContatoId:(int) usuContato;


@end
