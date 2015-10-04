//
//  ViewController.m
//  conex
//
//  Created by Larii on 23/09/12.
//  Copyright (c) 2012 Larii. All rights reserved.
//

#import "ViewController.h"
#import "ContatoController.h"
#import "SBJson.h"
#import "UsuarioDAO.h"
#import "Usuario.h"
#import "Contato.h"
#import "ContatoDAO.h"
#import "NSDataAdditions.h"
#import "Base64.h"
#import "Globais.h"
#import "ConexaoWebService.h"
#import "Reachability.h"
#import "MensagemDAO.h"


@interface ViewController ()

@end

@implementation ViewController

@synthesize usuarioDAO;
@synthesize contatoDAO;
@synthesize mensagemDAO;

@synthesize loginView, contatosView;

- (IBAction)valorSwitchAlterado:(id)sender{
    if(swtManterConectado.on){
        //lblManterConectado.text = @"Mantenha-me conectado";
        [swtManterConectado setOn:YES animated:YES];
        
    }else{
        //lblManterConectado.text = @"Mantenha-me desconectado";
        [swtManterConectado setOn:NO animated:YES];
    }

}


- (IBAction)doneTeclado:(id)sender{
    //desativar teclado independete do textfield que eu estiver usando
    [sender resignFirstResponder];
}


- (IBAction)criarConta:(id)sender{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://www.newconex.heliohost.org/inscricao.php"]];

}


/* Conecta Servidor*/

- (void) alertStatus:(NSString *)msg :(NSString *)title
{
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:title
                                                        message:msg
                                                       delegate:self
                                              cancelButtonTitle:@"Ok"
                                              otherButtonTitles:nil, nil];
    
    [alertView show];
}

BOOL gravaFoto(NSDictionary *jsonData, NSString *nomeFoto){
    
    @try {
        
        [Base64 initialize];
        NSData* data = [Base64 decode: [jsonData objectForKey:@"foto"]];
        UIImage* imagem = [UIImage imageWithData:data];
        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *documents = [paths objectAtIndex:0];
        NSString *finalPath = [documents stringByAppendingPathComponent:nomeFoto];
        [UIImageJPEGRepresentation(imagem, 1.0f) writeToFile:finalPath atomically:YES];
        return YES;
        
        
    }
    @catch (NSException* e) {
        return NO;
        NSLog(@"Exception: %@Falha ao Salvar Imagem", e);
    }

    
    
}

-(void) LoginOnline: (Usuario*)usuario{

        NSString *post =[[NSString alloc] initWithFormat:@"P1=%@&P2=%@",usuario.usuarioUsu,usuario.senhaUsu];
        NSURL *url=[NSURL URLWithString:@"http://www.newconex.heliohost.org/loginA.php"];
        
        ConexaoWebService *conectaWS = [[ConexaoWebService alloc]init];
        NSString* responseData = [conectaWS ConectaWS:post, url];
        
        SBJsonParser *jsonParser = [SBJsonParser new];
        NSDictionary *jsonData = (NSDictionary *) [jsonParser objectWithString:responseData error:nil];
        NSLog(@"%@",jsonData);
        
        
        NSInteger sucesso = [(NSNumber *) [jsonData objectForKey:@"sucesso"] integerValue];
        NSLog(@"%d",sucesso);
        if(sucesso == 1)
        {
            usuarioDAO = [[UsuarioDAO alloc] init];
            [usuarioDAO createDatabase];
            
            contatoDAO = [[ContatoDAO alloc] init];
            [contatoDAO createDatabase];
            
            mensagemDAO = [[MensagemDAO alloc] init];
            [mensagemDAO createDatabase];
            
            
            //Usuario *usuario;
            //usuario = [[Usuario alloc] init];
            
            
            NSArray* usuarioJson = [(NSDictionary*) [responseData JSONValue] objectForKey:@"usuario"];
            NSDictionary* usuarioData = [usuarioJson objectAtIndex:0];
            
            
            NSArray* fotoUsuarioJson = [(NSDictionary*) [responseData JSONValue] objectForKey:@"fotoUsuario"];
            NSDictionary* fotoUsuarioData = [fotoUsuarioJson  objectAtIndex:0];
            
            
            //grava foto usuario
            NSString * nomeFotoUsu = [NSString stringWithFormat:@"%@.%@", [fotoUsuarioData objectForKey:@"nomeFto"], [fotoUsuarioData objectForKey:@"tipoFto"]];
            
            
            BOOL resultadoGravaFto = gravaFoto(fotoUsuarioData, nomeFotoUsu);
            
            if (resultadoGravaFto)
                usuario.fotoUsu = nomeFotoUsu;
            else
                usuario.fotoUsu = nil;
            
        
            
            usuario.codigoUsu = [(NSNumber *) [usuarioData objectForKey:@"codigoUsu"] integerValue];
            usuario.usuarioUsu = [usuarioData objectForKey:@"usuarioUsu"];
            usuario.senhaUsu = [usuarioData objectForKey:@"senhaUsu"];
            usuario.nomeUsu = [usuarioData objectForKey:@"nomeUsu"];
            usuario.dataNascUsu = [usuarioData objectForKey:@"dataNasUcu"];
            usuario.codigoPaisUsu = [(NSNumber *) [usuarioData objectForKey:@"codigoPaisUsu"] integerValue];
            usuario.emailUsu = [usuarioData objectForKey:@"emailUsu"];
            usuario.sexoUsu = [usuarioData objectForKey:@"sexoUsu"];
            usuario.dataCadUsu =  [usuarioData objectForKey:@"dataCadUsu"];
            //Manter Conectado
            if (swtManterConectado.on) {
                usuario.manterConUsu = @"YES";
            }
            else
                usuario.manterConUsu = @"NO";
            
            
            [[Usuario usuarioGlobal] funUsuarioGlobal:usuario];
            [usuarioDAO  insertUser:usuario];
            
            NSArray* contatoJson = [(NSDictionary*) [responseData JSONValue] objectForKey:@"contatos"];
            
            int qtd = [contatoJson count];
            Contato *contato;
            for (int i = 0; i < qtd; i++) {
                
                
                contato = [[Contato alloc] init];
                
                NSDictionary* contatoData = [contatoJson objectAtIndex:i];
                
                NSLog(@"Nome: %@", [contatoData objectForKey:@"nomeUsuCtt"]);
                contato.codigoCtt = [(NSNumber *) [contatoData objectForKey:@"codigoCtt"] integerValue];
                contato.codigoUsuLocalCtt = [(NSNumber *) [contatoData objectForKey:@"codigoUsuLocalCtt"] integerValue];
                contato.statusCtt = [contatoData objectForKey:@"statusCtt"];
                contato.codigoUsuCtt = [(NSNumber *) [contatoData objectForKey:@"codigoUsuCtt"] integerValue];
                contato.usuarioUsuCtt = [contatoData objectForKey:@"usuarioUsuCtt"];
                
                NSLog(@"Nome 3: %@", [contatoData objectForKey:@"nomeUsuCtt"]);
                contato.nomeUsuCtt = [contatoData objectForKey:@"nomeUsuCtt"];
                NSLog(@"Nome 4: %@", contato.nomeUsuCtt);
                
                
                contato.dataNascUsuCtt = [contatoData objectForKey:@"dataNascUsuCtt"];
                contato.emailUsuCtt = [contatoData objectForKey:@"emailUsuCtt"];
                contato.sexoUsuCtt = [contatoData objectForKey:@"sexoUsuCtt"];
                contato.dataCadUsuCtt = [contatoData objectForKey:@"dataCadUsuCtt"];
                
                
                //Foto
                if([(NSNumber *) [contatoData objectForKey:@"qtdFoto"] integerValue] > 0)
                {
                    NSString * nomeFotoCtt = [NSString stringWithFormat:@"%@.%@", [contatoData objectForKey:@"nomeFto"], [contatoData   objectForKey:@"tipoFto"]];
                    
                    if (gravaFoto(contatoData, nomeFotoCtt))
                        contato.nomeFotoCtt = nomeFotoCtt;
                    else
                        NSLog(@"Erro ao gravar imagem de: %@", contato.nomeUsuCtt);
                }
                
                else
                    contato.nomeFotoCtt = @"anonimo.png";
                
                
                NSLog(@"Contato: %@", contato.nomeUsuCtt);
                
                
                [contatoDAO  insertContato:contato];
                
                
            }
            
            
            
            
            NSArray* mensagemJson = [(NSDictionary*) [responseData JSONValue] objectForKey:@"msg"];
            
            for (int i = 0; i < [mensagemJson count]; i++) {
                
                
                Mensagem *msg;
                msg = [[Mensagem alloc] init];
                NSDictionary* msgData = [mensagemJson objectAtIndex:i];
                
                msg.codigoMsg = [(NSNumber *) [msgData objectForKey:@"codigoMsg"] integerValue];
                msg.usuOrig = [contatoDAO getContatoId:[(NSNumber *) [msgData objectForKey:@"codigoUsuOriMsg"] integerValue]];
                msg.dataMsg = [msgData objectForKey:@"dataMsg"];
                msg.textoMsg = [msgData objectForKey:@"textoMsg"];
                msg.statusLida = @"N";
                [mensagemDAO  insertMensagem:msg, usuario.codigoUsu];
                
                
            }
            
            
            
            
          [self performSegueWithIdentifier: @"contatoView" sender: self];
            
            
            
        } else {
            
            NSString *error_msg = (NSString *) [jsonData objectForKey:@"error_message"];
            [self alertStatus:error_msg :@"Login Falhou!"];
        }

    
}







- (IBAction)loginClicked:(id)sender {
    
    
    if([[txtUsuario text] isEqualToString:@""] || [[txtSenha text] isEqualToString:@""] ) {
        [self alertStatus:@"Por favor entre com o usuário e senha" :@"Login Falhou!"];
    }
    
    Usuario *usuario;
    usuario = [[Usuario alloc] init];
    usuario.usuarioUsu = [txtUsuario text] ;
    usuario.senhaUsu = [txtSenha text] ;
    [self efetuaLogin:usuario];
        
}


- (void) efetuaLogin: (Usuario *) usuario{
    Reachability* reach = [Reachability reachabilityWithHostname:@"www.google.com"];
    NetworkStatus netStatus = [reach currentReachabilityStatus];
    if(netStatus != NotReachable){
        [self LoginOnline:usuario];
    }    else {
        UsuarioDAO *usuDAO = [[UsuarioDAO alloc]init];
        Usuario* usuario = [usuDAO autenticaUsu: [txtUsuario text]:[txtSenha text]];
        if([usuario.usuarioUsu isEqual:[txtUsuario text]]){
            
            [[Usuario usuarioGlobal] funUsuarioGlobal:usuario];
            NSLog(@"Autenticou OFF!");
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Aviso"
                                                            message:@"Você está conectado em modo Off line"                                                              delegate:nil
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];
        }
    }


}

- (IBAction)ativaTeclado:(id)sender{
    
    CGFloat height = (self.view.frame.size.height/1.8);
    CGFloat width = self.view.frame.size.width;
    
    CGRect halfFrame = CGRectMake(0, 0, width, height);
    self.view.frame = halfFrame;
}

- (IBAction)desativaTeclado:(id)sender{
    CGFloat height = (self.view.frame.size.height*1.8);
    CGFloat width = self.view.frame.size.width;
    
    CGRect fullFrame = CGRectMake(0, 0, width, height);
    self.view.frame = fullFrame;
    
}

-(void) viewDidAppear:(BOOL)animated{
        
    //NSString * userUsu = txtUsuario.text;
    //NSString * senhaUsu = txtSenha.text;
    UsuarioDAO *usuDAO = [[UsuarioDAO alloc]init];
    Usuario *usuario = [usuDAO conectaUsu ];
    if([usuario.manterConUsu isEqualToString:@"YES"]){
        
        txtUsuario.text = usuario.usuarioUsu;
        txtSenha.text = usuario.senhaUsu;
        [self efetuaLogin:usuario];
    }
    
   }



- (void)viewDidLoad
{
    [super viewDidLoad];

        
    
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    
    
    
    

    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

@end





/*
 
 
 
 
 NSString * userUsu = txtUsuario.text;
 NSString * senhaUsu = txtSenha.text;
   UsuarioDAO *usuDAO = [[UsuarioDAO alloc]init];
 Usuario *usu = [usuDAO conectaUsu ];
 
 if([usu.manterConUsu isEqualToString:@"YES"]){
 
 userUsu = usu.usuarioUsu;
 senhaUsu = usu.senhaUsu;
 
 }
 */

// allocate a reachability object
/*Reachability* reach = [Reachability reachabilityWithHostname:@"www.google.com"];

NetworkStatus netStatus = [reach currentReachabilityStatus];


if(netStatus == NotReachable){
    
    UsuarioDAO *usuDAO = [[UsuarioDAO alloc]init];
    Usuario* usuarioUsu = [usuDAO autenticaUsu:userUsu:senhaUsu];
    
    if([usuarioUsu.usuarioUsu isEqual:[txtUsuario text]]){
        
        [[Usuario usuarioGlobal] funUsuarioGlobal:usuarioUsu];
        NSLog(@"Autenticou OFF!");
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Aviso"
                                                        message:@"Você está conectado em modo Off line"                                                              delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
        [self performSegueWithIdentifier: @"contatoView" sender: self];
    }
    else{
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Aviso"
                                                        message:@"Verifique sua conexão com a internet"                                                              delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
        
    }
    
    
}
else
{
    NSString *post =[[NSString alloc] initWithFormat:@"P1=%@&P2=%@",[txtUsuario text],[txtSenha text]];
    NSURL *url=[NSURL URLWithString:@"http://www.newconex.heliohost.org/loginA.php"];
    
    ConexaoWebService *conectaWS = [[ConexaoWebService alloc]init];
    NSString* responseData = [conectaWS ConectaWS:post, url];
    
    SBJsonParser *jsonParser = [SBJsonParser new];
    NSDictionary *jsonData = (NSDictionary *) [jsonParser objectWithString:responseData error:nil];
    NSLog(@"%@",jsonData);
    
    
    NSInteger sucesso = [(NSNumber *) [jsonData objectForKey:@"sucesso"] integerValue];
    NSLog(@"%d",sucesso);
    if(sucesso == 1)
    {
        usuarioDAO = [[UsuarioDAO alloc] init];
        [usuarioDAO createDatabase];
        
        contatoDAO = [[ContatoDAO alloc] init];
        [contatoDAO createDatabase];
        
        mensagemDAO = [[MensagemDAO alloc] init];
        [mensagemDAO createDatabase];
        
        
        Usuario *usuario;
        usuario = [[Usuario alloc] init];
        
        
        NSArray* usuarioJson = [(NSDictionary*) [responseData JSONValue] objectForKey:@"usuario"];
        NSDictionary* usuarioData = [usuarioJson objectAtIndex:0];
        
        
        NSArray* fotoUsuarioJson = [(NSDictionary*) [responseData JSONValue] objectForKey:@"fotoUsuario"];
        NSDictionary* fotoUsuarioData = [fotoUsuarioJson  objectAtIndex:0];
        
        
        //grava foto usuario
        NSString * nomeFotoUsu = [NSString stringWithFormat:@"%@.%@", [fotoUsuarioData objectForKey:@"nomeFto"], [fotoUsuarioData objectForKey:@"tipoFto"]];
        
        
        BOOL resultadoGravaFto = gravaFoto(fotoUsuarioData, nomeFotoUsu);
        
        if (resultadoGravaFto)
            usuario.fotoUsu = nomeFotoUsu;
        else
            usuario.fotoUsu = nil;
        
        
        
        usuario.codigoUsu = [(NSNumber *) [usuarioData objectForKey:@"codigoUsu"] integerValue];
        usuario.usuarioUsu = [usuarioData objectForKey:@"usuarioUsu"];
        usuario.senhaUsu = [usuarioData objectForKey:@"senhaUsu"];
        usuario.nomeUsu = [usuarioData objectForKey:@"nomeUsu"];
        usuario.dataNascUsu = [usuarioData objectForKey:@"dataNasUcu"];
        usuario.codigoPaisUsu = [(NSNumber *) [usuarioData objectForKey:@"codigoPaisUsu"] integerValue];
        usuario.emailUsu = [usuarioData objectForKey:@"emailUsu"];
        usuario.sexoUsu = [usuarioData objectForKey:@"sexoUsu"];
        usuario.dataCadUsu =  [usuarioData objectForKey:@"dataCadUsu"];
        //Manter Conectado
        if (swtManterConectado.on) {
            usuario.manterConUsu = @"YES";
        }
        else
            usuario.manterConUsu = @"NO";
        
        
        [[Usuario usuarioGlobal] funUsuarioGlobal:usuario];
        [usuarioDAO  insertUser:usuario];
        
        NSArray* contatoJson = [(NSDictionary*) [responseData JSONValue] objectForKey:@"contatos"];
        
        int qtd = [contatoJson count];
        Contato *contato;
        for (int i = 0; i < qtd; i++) {
            
            
            contato = [[Contato alloc] init];
            
            NSDictionary* contatoData = [contatoJson objectAtIndex:i];
            
            NSLog(@"Nome: %@", [contatoData objectForKey:@"nomeUsuCtt"]);
            contato.codigoCtt = [(NSNumber *) [contatoData objectForKey:@"codigoCtt"] integerValue];
            contato.codigoUsuLocalCtt = [(NSNumber *) [contatoData objectForKey:@"codigoUsuLocalCtt"] integerValue];
            contato.statusCtt = [contatoData objectForKey:@"statusCtt"];
            contato.codigoUsuCtt = [(NSNumber *) [contatoData objectForKey:@"codigoUsuCtt"] integerValue];
            contato.usuarioUsuCtt = [contatoData objectForKey:@"usuarioUsuCtt"];
            
            NSLog(@"Nome 3: %@", [contatoData objectForKey:@"nomeUsuCtt"]);
            contato.nomeUsuCtt = [contatoData objectForKey:@"nomeUsuCtt"];
            NSLog(@"Nome 4: %@", contato.nomeUsuCtt);
            
            
            contato.dataNascUsuCtt = [contatoData objectForKey:@"dataNascUsuCtt"];
            contato.emailUsuCtt = [contatoData objectForKey:@"emailUsuCtt"];
            contato.sexoUsuCtt = [contatoData objectForKey:@"sexoUsuCtt"];
            contato.dataCadUsuCtt = [contatoData objectForKey:@"dataCadUsuCtt"];
            
            
            //Foto
            if([(NSNumber *) [contatoData objectForKey:@"qtdFoto"] integerValue] > 0)
            {
                NSString * nomeFotoCtt = [NSString stringWithFormat:@"%@.%@", [contatoData objectForKey:@"nomeFto"], [contatoData   objectForKey:@"tipoFto"]];
                
                if (gravaFoto(contatoData, nomeFotoCtt))
                    contato.nomeFotoCtt = nomeFotoCtt;
                else
                    NSLog(@"Erro ao gravar imagem de: %@", contato.nomeUsuCtt);
            }
            
            else
                contato.nomeFotoCtt = @"anonimo.png";
            
            
            NSLog(@"Contato: %@", contato.nomeUsuCtt);
            
            
            [contatoDAO  insertContato:contato];
            
            
        }
        
        
        
        
        NSArray* mensagemJson = [(NSDictionary*) [responseData JSONValue] objectForKey:@"msg"];
        
        for (int i = 0; i < [mensagemJson count]; i++) {
            
            
            Mensagem *msg;
            msg = [[Mensagem alloc] init];
            NSDictionary* msgData = [mensagemJson objectAtIndex:i];
            
            msg.codigoMsg = [(NSNumber *) [msgData objectForKey:@"codigoMsg"] integerValue];
            msg.usuOrig = [contatoDAO getContatoId:[(NSNumber *) [msgData objectForKey:@"codigoUsuOriMsg"] integerValue]];
            msg.dataMsg = [msgData objectForKey:@"dataMsg"];
            msg.textoMsg = [msgData objectForKey:@"textoMsg"];
            msg.statusLida = @"N";
            [mensagemDAO  insertMensagem:msg, usuario.codigoUsu];
            
            
        }
        
        
        
        
        [self performSegueWithIdentifier: @"contatoView" sender: self];
        
        
        
    } else {
        
        NSString *error_msg = (NSString *) [jsonData objectForKey:@"error_message"];
        [self alertStatus:error_msg :@"Login Falhou!"];
    }
    
}





*/