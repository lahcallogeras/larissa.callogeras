//
//  UsuarioDAO.h
//  conex
//
//  Created by Gleison on 02/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "sqlite3.h"
#import "Usuario.h"


@interface UsuarioDAO : NSObject{
    sqlite3 *conexDB;
    NSString *databasePath;
}

- (void)createDatabase;
- (void)insertUser:(Usuario *)usuario;
- (void) limpaManterConectado;
- (Usuario*)autenticaUsu:(NSString*) usuarioUsu: (NSString*)senhaUsu;
//- (void)updateUser:(Usuario *)usuario;
//- (void)deleteUser:(Usuario *)usuario;
- (NSArray *)getUser;
- (Usuario*) conectaUsu;


@end


