//
//  Usuario.m
//  conex
//
//  Created by Gleison on 02/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import "Usuario.h"

@implementation Usuario

@synthesize usuarioUsu, senhaUsu, nomeUsu, dataNascUsu, codigoPaisUsu,
emailUsu, sexoUsu, manterConUsu, dataCadUsu, dataLoginUsu, fotoUsu, codigoUsu;



//Funçoes usuario Global
static Usuario *usuarioGlobal = nil;

+ (Usuario*)usuarioGlobal {
    if (usuarioGlobal == nil) {
        usuarioGlobal = [[super allocWithZone:NULL] init];
        
        // initialize your variables here
        usuarioGlobal.nomeUsu = @"";
    }
    return usuarioGlobal;
}
/*
+ (id)allocWithZone:(NSZone *)zone {
	@synchronized(self)
	{
		if (usuarioGlobal == nil)
		{
			usuarioGlobal = [super allocWithZone:zone];
			return usuarioGlobal;
		}
	}
	return nil;
} */

-(void)funUsuarioGlobal:(Usuario*)usuario;{
    self.codigoUsu = usuario.codigoUsu;
    self.nomeUsu = usuario.usuarioUsu;
    self.fotoUsu = usuario.fotoUsu;
    
}




//Fim Funçoes usuario Global



@end

